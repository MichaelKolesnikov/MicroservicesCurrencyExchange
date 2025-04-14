package kfd.userservice.service

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "payment-service")
interface PaymentService {
    @GetMapping(value =["/some"])
    fun getSomeFromPaymentService(): String
}