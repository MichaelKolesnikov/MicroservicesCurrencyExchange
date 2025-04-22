package kfd.userservice.controller

import io.micrometer.observation.annotation.Observed
import kfd.userservice.enums.Currency
import kfd.userservice.model.UserDTO
import kfd.userservice.repository.ServiceCurrencyRepository
import kfd.userservice.repository.UserCurrencyRepository
import kfd.userservice.service.PaymentService
import kfd.userservice.service.RegisterService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@Observed(name = "ExchangeController")
class UserController(
    private val registerService: RegisterService,
    private val userCurrencyRepo: UserCurrencyRepository,
    private val serviceCurrencyRepo: ServiceCurrencyRepository,
    private val paymentService: PaymentService
) {
    @GetMapping("/home")
    fun homePage(
        @AuthenticationPrincipal userDetails: UserDetails,
        model: Model
    ): String {
        val user = registerService.findByUsername(userDetails.username)
            ?: return "redirect:/login"

        val userCurrencies = userCurrencyRepo.findByUser(user).associateBy({ it.currency }, { it.amount })
        val serviceCurrencies = serviceCurrencyRepo.findAll().associateBy({ it.currency }, { it.amount })

        val exchangeRates = paymentService.getExchangeRates()

        model.addAttribute("userCurrencies", userCurrencies)
        model.addAttribute("serviceCurrencies", serviceCurrencies)
        model.addAttribute("rates", exchangeRates)
        model.addAttribute("currencyList", Currency.entries)
        return "home"
    }

    @PostMapping("/exchange")
    fun exchangeCurrency(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam fromCurrency: Currency,
        @RequestParam toCurrency: Currency,
        @RequestParam amount: Double,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        val user = registerService.findByUsername(userDetails.username) ?: return "redirect:/login"
        val userDto = UserDTO(
            id = user.id,
            username = user.username,
            password = user.password
        )
        val answer = paymentService.exchangeCurrency(userDto, fromCurrency, toCurrency, amount)
        if (answer.isNullOrEmpty()) {
            redirectAttributes.addFlashAttribute("success", "Exchange successful!")
        }
        else {
            redirectAttributes.addFlashAttribute("error", answer)
        }
        return "redirect:/home"
    }

    @PostMapping("/addFunds")
    fun addFunds(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam currency: Currency,
        @RequestParam amount: Double,
        redirectAttributes: RedirectAttributes
    ): String {
        val user = registerService.findByUsername(userDetails.username) ?: return "redirect:/login"
        val userDto = UserDTO(
            id = user.id,
            username = user.username,
            password = user.password
        )
        val answer = paymentService.addFunds(userDto, currency, amount)
        if (answer.isNullOrEmpty()) {
            redirectAttributes.addFlashAttribute("success", "Funds added successfully!")
        } else {
            redirectAttributes.addFlashAttribute("error", answer)
        }
        return "redirect:/home"
    }
}