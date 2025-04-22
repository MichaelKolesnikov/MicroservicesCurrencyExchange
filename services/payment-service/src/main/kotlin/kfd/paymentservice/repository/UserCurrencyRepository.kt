package kfd.paymentservice.repository


import kfd.paymentservice.enums.Currency
import kfd.paymentservice.model.User
import kfd.paymentservice.model.UserCurrency
import org.springframework.data.jpa.repository.JpaRepository

interface UserCurrencyRepository : JpaRepository<UserCurrency, Long> {
    fun findByUserAndCurrency(user: User, currency: Currency): UserCurrency?
    fun findByUser(user: User): List<UserCurrency>
}