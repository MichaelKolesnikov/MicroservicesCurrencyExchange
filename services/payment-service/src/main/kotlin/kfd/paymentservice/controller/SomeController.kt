package kfd.paymentservice.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SomeController {
    @GetMapping("/some")
    fun getPaymentService() = ResponseEntity.ok("I'm payment service")
}