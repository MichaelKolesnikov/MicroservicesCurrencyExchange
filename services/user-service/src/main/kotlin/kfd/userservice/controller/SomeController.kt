package kfd.userservice.controller

import kfd.userservice.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SomeController (
    private val paymentService: PaymentService
) {
    @GetMapping("/payment")
    fun getPaymentService() = ResponseEntity.ok(paymentService.getSomeFromPaymentService())
}