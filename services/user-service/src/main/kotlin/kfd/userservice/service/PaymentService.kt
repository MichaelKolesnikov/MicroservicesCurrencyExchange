package kfd.userservice.service

import io.micrometer.observation.annotation.Observed
import kfd.userservice.enums.Currency
import kfd.userservice.model.MoneyRecord
import kfd.userservice.model.ServiceCurrency
import kfd.userservice.model.User
import kfd.userservice.model.UserDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@Observed(name = "PaymentService")
@FeignClient(name = "payment-service")
interface PaymentService {
    @GetMapping(value =["/some"])
    fun getSomeFromPaymentService(): String

    @PostMapping(value = ["/exchange"])
    fun exchangeCurrency(
        @RequestBody user: UserDTO,
        @RequestParam fromCurrency: Currency,
        @RequestParam toCurrency: Currency,
        @RequestParam amount: Double
    ): String?

    @GetMapping(value = ["exchange-rates"])
    fun getExchangeRates(): Map<Currency, Map<Currency, Double>>

    @PostMapping(value = ["/add-funds"])
    fun addFunds(
        @RequestBody user: UserDTO,
        @RequestParam currency: Currency,
        @RequestParam amount: Double
    ): String?

}