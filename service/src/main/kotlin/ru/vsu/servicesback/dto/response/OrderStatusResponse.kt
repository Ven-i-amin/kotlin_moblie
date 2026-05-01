package ru.vsu.servicesback.dto.response

import ru.vsu.servicesback.util.OrderStatus
import java.time.LocalDateTime

data class OrderStatusResponse(
    val id: Long,
    val status: OrderStatus,
    val executedAt: LocalDateTime?,
)
