package kfd.paymentservice.controller

import io.micrometer.observation.annotation.Observed
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Observed(name = "SomeController")
class SomeController {
    @GetMapping("/some")
    fun getPaymentService() = ResponseEntity.ok("I'm payment service")
}