package kfd.userservice.repository

import kfd.userservice.enums.Currency
import kfd.userservice.model.ServiceCurrency
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceCurrencyRepository : JpaRepository<ServiceCurrency, Long> {
    fun findByCurrency(currency: Currency): ServiceCurrency?
}