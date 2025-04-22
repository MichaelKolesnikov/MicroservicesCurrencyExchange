package kfd.paymentservice.model

import jakarta.persistence.*
import kfd.paymentservice.enums.Currency

@Entity
@Table(name = "service_currency")
data class ServiceCurrency(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    val currency: Currency,

    @Embedded
    var amount: MoneyRecord
)