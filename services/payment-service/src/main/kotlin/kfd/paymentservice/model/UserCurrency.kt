package kfd.paymentservice.model

import jakarta.persistence.*
import kfd.paymentservice.enums.Currency

@Entity
@Table(name = "user_currency")
data class UserCurrency(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Enumerated(EnumType.STRING)
    val currency: Currency,

    @Embedded
    var amount: MoneyRecord
)