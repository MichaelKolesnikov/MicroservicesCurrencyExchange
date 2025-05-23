package kfd.userservice.repository

import io.micrometer.observation.annotation.Observed
import kfd.userservice.enums.Currency
import kfd.userservice.model.User
import kfd.userservice.model.UserCurrency
import org.springframework.data.jpa.repository.JpaRepository

@Observed(name = "UserCurrencyRepository")
interface UserCurrencyRepository : JpaRepository<UserCurrency, Long> {
    fun findByUserAndCurrency(user: User, currency: Currency): UserCurrency?
    fun findByUser(user: User): List<UserCurrency>
}