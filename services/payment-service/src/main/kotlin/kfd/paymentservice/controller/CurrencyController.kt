package kfd.paymentservice.controller

import io.micrometer.observation.annotation.Observed
import kfd.paymentservice.enums.Currency
import kfd.paymentservice.model.*
import kfd.paymentservice.repository.ServiceCurrencyRepository
import kfd.paymentservice.repository.UserCurrencyRepository
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@Controller
@Observed(name = "CurrencyController")
class CurrencyController(
    private val userCurrencyRepo: UserCurrencyRepository,
    private val serviceCurrencyRepo: ServiceCurrencyRepository
) {
    private val exchangeRates: Map<Currency, Map<Currency, Double>> = mapOf(
        Currency.RUB to mapOf(
            Currency.USD to 1.0 / 90.0,
            Currency.EUR to 1.0 / 100.0
        ),
        Currency.USD to mapOf(
            Currency.RUB to 90.0,
            Currency.EUR to 0.9,
            Currency.USDT to 1.0,
            Currency.BTC to 1.0 / 59112.0
        ),
        Currency.EUR to mapOf(
            Currency.RUB to 100.0,
            Currency.USD to 1.0 / 9.0
        ),
        Currency.USDT to mapOf(
            Currency.USD to 1.0
        ),
        Currency.BTC to mapOf(
            Currency.USD to 59112.0
        )
    )

    @GetMapping("exchange-rates")
    @ResponseBody
    fun getExchangeRates() = exchangeRates


    @Transactional
    @PostMapping("/add-funds")
    @ResponseBody
    fun addFunds(
        @RequestBody userDto: UserDTO,
        @RequestParam currency: Currency,
        @RequestParam amount: Double
    ): String? {
        val user = User(
            id = userDto.id,
            username = userDto.username,
            password = userDto.password
        )
        var message: String? = null
        try {
            val totalAmount = (amount * 100).toLong()
            val userCurrency = userCurrencyRepo.findByUserAndCurrency(user, currency)
                ?: UserCurrency(user = user, currency = currency, amount = MoneyRecord(0))

            userCurrency.amount += MoneyRecord(totalAmount)
            userCurrencyRepo.save(userCurrency)
        }
        catch (e: Exception) {
            message = e.message
        }
        return message
    }

    @Transactional
    @PostMapping("/exchange")
    @ResponseBody
    fun exchangeCurrency(
        @RequestBody userDto: UserDTO,
        @RequestParam fromCurrency: Currency,
        @RequestParam toCurrency: Currency,
        @RequestParam amount: Double
    ): String? {
        try {
            val user = User(
                id = userDto.id,
                username = userDto.username,
                password = userDto.password
            )
            val from = userCurrencyRepo.findByUserAndCurrency(user, fromCurrency)
                ?: throw IllegalArgumentException("Currency not found")

            val serviceFrom = serviceCurrencyRepo.findByCurrency(fromCurrency)
                ?: throw IllegalStateException("Service currency error")

            val rate = exchangeRates[fromCurrency]?.get(toCurrency)
                ?: throw IllegalArgumentException("Exchange rate not available")

            val totalAmount = (amount * 100).toLong()
            if (from.amount.amountInSubunits < totalAmount) {
                throw IllegalArgumentException("Insufficient funds")
            }

            val convertedAmount = (totalAmount * rate).toLong()

            val serviceTo = serviceCurrencyRepo.findByCurrency(toCurrency)
                ?: throw IllegalStateException("Service currency error")

            if (serviceTo.amount.amountInSubunits < convertedAmount) {
                throw IllegalArgumentException("Service out of currency")
            }

            from.amount -= MoneyRecord(totalAmount)
            serviceFrom.amount += MoneyRecord(totalAmount)

            val to = userCurrencyRepo.findByUserAndCurrency(user, toCurrency) ?:
            UserCurrency(user = user, currency = toCurrency, amount = MoneyRecord(0))

            to.amount += MoneyRecord(convertedAmount)
            serviceTo.amount -= MoneyRecord(convertedAmount)

            userCurrencyRepo.save(from)
            userCurrencyRepo.save(to)
            serviceCurrencyRepo.save(serviceFrom)
            serviceCurrencyRepo.save(serviceTo)
            return null
        } catch (e: Exception) {
            return e.message
        }
    }
}