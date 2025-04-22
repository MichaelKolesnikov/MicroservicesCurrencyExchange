package kfd.userservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import kfd.userservice.model.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}
