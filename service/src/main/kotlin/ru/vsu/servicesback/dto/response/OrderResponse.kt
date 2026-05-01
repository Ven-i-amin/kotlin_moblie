package ru.vsu.servicesback.dto.response

import ru.vsu.servicesback.util.OrderStatus
import java.time.LocalDateTime

data class OrderResponse(
    val id: Long,
    val currencyId: String,
    val amount: Double,
    val exchangeCurrencyId: String,
    val exchangeAmount: Double,
    val type: String,
    val status: OrderStatus,
    val email: String,
    val timestamp: LocalDateTime?,
)
