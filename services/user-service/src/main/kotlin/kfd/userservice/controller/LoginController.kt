package kfd.userservice.controller

import io.micrometer.observation.annotation.Observed
import kfd.userservice.service.RegisterService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@Observed(name = "LoginController")
class LoginController(private val userService: RegisterService) {

    @GetMapping("/register")
    fun registerForm(): String {
        return "register"
    }

    @PostMapping("/register")
    fun registerUser(
        @RequestParam username: String,
        @RequestParam password: String,
        model: Model
    ): String {
        return try {
            val user = userService.registerUser(username, password)
            model.addAttribute("user", user)
            "redirect:/home"
        } catch (e: IllegalArgumentException) {
            model.addAttribute("error", e.message)
            "register"
        }
    }

    @GetMapping("/login")
    fun loginForm(): String {
        return "login"
    }
}