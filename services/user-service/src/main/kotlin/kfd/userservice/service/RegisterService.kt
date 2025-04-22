package kfd.userservice.service

import io.micrometer.observation.annotation.Observed
import kfd.userservice.enums.Currency
import kfd.userservice.model.MoneyRecord
import kfd.userservice.model.User
import kfd.userservice.model.UserCurrency
import kfd.userservice.repository.UserCurrencyRepository
import kfd.userservice.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Observed(name = "RegisterService")
class RegisterService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userCurrencyRepository: UserCurrencyRepository
) {
    @Transactional
    fun registerUser(username: String, password: String): User {
        val existingUser = userRepository.findByUsername(username)
        if (existingUser != null) {
            throw IllegalArgumentException("Пользователь с именем $username уже существует")
        }
        val encodedPassword = passwordEncoder.encode(password)
        val user = User(username = username, password = encodedPassword)
        val savedUser = userRepository.save(user)

        val initialCurrencies = Currency.entries.map { currency ->
            UserCurrency(user = savedUser, currency = currency, amount = MoneyRecord(0))
        }

        userCurrencyRepository.saveAll(initialCurrencies)

        return savedUser
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }
}