package kfd.userservice.service

import io.micrometer.observation.annotation.Observed
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@Observed(name = "PaymentService")
@FeignClient(name = "payment-service")
interface PaymentService {
    @GetMapping(value =["/some"])
    fun getSomeFromPaymentService(): String
}