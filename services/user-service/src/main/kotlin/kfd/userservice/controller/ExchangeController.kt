package kfd.userservice.controller

import io.micrometer.observation.annotation.Observed
import kfd.userservice.enums.Currency
import kfd.userservice.service.CurrencyService
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
class ExchangeController(
    private val currencyService: CurrencyService,
    private val userService: RegisterService
) {
    @GetMapping("/home")
    fun homePage(
        @AuthenticationPrincipal userDetails: UserDetails,
        model: Model
    ): String {
        val user = userService.findByUsername(userDetails.username)
            ?: return "redirect:/login"

        val userCurrencies = currencyService.getUserCurrencies(user).associateBy({ it.currency }, { it.amount })
        val serviceCurrencies = currencyService.getServiceCurrencies().associateBy({ it.currency }, { it.amount })

        val exchangeRates = currencyService.getExchangeRates()

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
        val user = userService.findByUsername(userDetails.username) ?: return "redirect:/login"

        try {
            currencyService.exchangeCurrency(user, fromCurrency, toCurrency, amount)
            redirectAttributes.addFlashAttribute("success", "Exchange successful!")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", e.message)
        }
        return "redirect:/home"
    }

    @PostMapping("/addFunds")
    fun addFunds(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam currency: Currency,
        @RequestParam amount: Double,
        model: Model
    ): String {
        val user = userService.findByUsername(userDetails.username)
            ?: return "redirect:/login"

        return try {
            currencyService.addFunds(user, currency, amount)
            model.addAttribute("success", "Funds added successfully!")
            "redirect:/home"
        } catch (e: Exception) {
            model.addAttribute("error", e.message)
            "home"
        }
    }
}