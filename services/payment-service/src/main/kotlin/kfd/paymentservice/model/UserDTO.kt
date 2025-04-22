package kfd.paymentservice.model

data class UserDTO(
    val id: Long = 0,
    val username: String,
    val password: String
)