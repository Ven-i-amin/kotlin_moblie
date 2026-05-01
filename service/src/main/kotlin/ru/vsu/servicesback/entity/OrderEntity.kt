package ru.vsu.servicesback.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import ru.vsu.servicesback.util.OrderStatus
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "currency_id", nullable = false)
    var currencyId: String = "",
    @Column(name = "currency_name", nullable = false)
    var currencyName: String = "",
    @Column(nullable = false)
    var amount: Double = 0.0,
    @Column(name = "exchange_currency_id", nullable = false)
    var exchangeCurrencyId: String = "",
    @Column(name = "exchange_amount", nullable = false)
    var exchangeAmount: Double = 0.0,
    @Column(nullable = false)
    var type: String = "",
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.STARTED,
    @Column(nullable = false)
    var email: String = "",
    @Column(name = "executed_at")
    var executedAt: LocalDateTime? = null,
    @Column(name = "finished_at")
    var finishedAt: LocalDateTime? = null,
)
