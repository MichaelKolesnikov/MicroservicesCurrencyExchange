package kfd.paymentservice.repository


import kfd.paymentservice.enums.Currency
import kfd.paymentservice.model.ServiceCurrency
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceCurrencyRepository : JpaRepository<ServiceCurrency, Long> {
    fun findByCurrency(currency: Currency): ServiceCurrency?
}