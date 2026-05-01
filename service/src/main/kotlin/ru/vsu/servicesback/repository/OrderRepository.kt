package ru.vsu.servicesback.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.vsu.servicesback.entity.OrderEntity
import ru.vsu.servicesback.util.OrderStatus
import java.time.LocalDateTime

interface OrderRepository : JpaRepository<OrderEntity, Long> {
    fun findAllByEmail(email: String): List<OrderEntity>
    fun findTop10ByEmailAndStatusInAndExecutedAtLessThanEqualOrderByExecutedAtAsc(
        email: String,
        statuses: Collection<OrderStatus>,
        executedAt: LocalDateTime,
    ): List<OrderEntity>

    fun findTop10ByStatusInAndExecutedAtLessThanEqualOrderByExecutedAtAsc(
        statuses: Collection<OrderStatus>,
        executedAt: LocalDateTime,
    ): List<OrderEntity>

    fun deleteAllByEmail(email: String)
}
