package kfd.userservice.repository

import io.micrometer.observation.annotation.Observed
import kfd.userservice.enums.Currency
import kfd.userservice.model.ServiceCurrency
import org.springframework.data.jpa.repository.JpaRepository

@Observed(name = "ServiceCurrencyRepository")
interface ServiceCurrencyRepository : JpaRepository<ServiceCurrency, Long> {
    fun findByCurrency(currency: Currency): ServiceCurrency?
}