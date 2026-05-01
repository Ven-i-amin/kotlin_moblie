package ru.vsu.servicesback.service

import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.vsu.servicesback.dto.request.OrderRequest
import ru.vsu.servicesback.dto.response.OrderResponse
import ru.vsu.servicesback.dto.response.OrderStatusResponse
import ru.vsu.servicesback.entity.OrderEntity
import ru.vsu.servicesback.repository.OrderRepository
import ru.vsu.servicesback.repository.UserCoinRepository
import ru.vsu.servicesback.repository.UserRepository
import ru.vsu.servicesback.security.AuthContext
import ru.vsu.servicesback.util.OrderStatus
import java.time.LocalDateTime
import java.util.Random

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val userCoinRepository: UserCoinRepository,
    private val random: Random,
    private val authContext: AuthContext,
) {

    fun getById(orderId: Long): OrderResponse {
        val order = getEntityById(orderId)
        authContext.requireEmail(order.email)
        return order.toResponse()
    }

    fun getAll(): List<OrderResponse> =
        orderRepository.findAllByEmail(authContext.currentUserEmail()).map { it.toResponse() }

    fun getAllByEmail(email: String): List<OrderResponse> {
        authContext.requireEmail(email)
        return orderRepository.findAllByEmail(email).map { it.toResponse() }
    }

    fun getStatusesReadyForProcessing(): List<OrderStatusResponse> =
        orderRepository.findTop10ByEmailAndStatusInAndExecutedAtLessThanEqualOrderByExecutedAtAsc(
            authContext.currentUserEmail(),
            listOf(OrderStatus.STARTED, OrderStatus.PROCESSING),
            LocalDateTime.now(),
        ).map {
            OrderStatusResponse(
                id = requireNotNull(it.id),
                status = it.status,
                executedAt = it.executedAt,
            )
        }

    @Transactional
    fun create(request: OrderRequest) {
        authContext.requireEmail(request.email)
        orderRepository.save(
            OrderEntity(
                currencyId = request.currencyId,
                currencyName = request.currencyId,
                amount = request.amount,
                exchangeCurrencyId = request.exchangeCurrencyId,
                exchangeAmount = request.exchangeAmount,
                type = request.type,
                status = OrderStatus.STARTED,
                email = request.email,
                executedAt = LocalDateTime.now(),
            ),
        )
    }

    @Transactional
    fun update(id: Long, request: OrderRequest) {
        val order = getEntityById(id)
        authContext.requireEmail(order.email)
        authContext.requireEmail(request.email)
        order.currencyId = request.currencyId
        order.currencyName = request.currencyId
        order.amount = request.amount
        order.exchangeCurrencyId = request.exchangeCurrencyId
        order.exchangeAmount = request.exchangeAmount
        order.type = request.type
        order.email = request.email

        orderRepository.save(order)
    }

    @Transactional
    fun setCurrencyAmount(orderId: Long, amount: Double) {
        val order = getEntityById(orderId)
        authContext.requireEmail(order.email)
        val newAmount = order.amount + amount
        if (newAmount < 0) {
            throw ArithmeticException("Amount is negative")
        }

        order.amount = newAmount
        orderRepository.save(order)
    }

    @Transactional
    fun setExchangeAmount(orderId: Long, amount: Double) {
        val order = getEntityById(orderId)
        authContext.requireEmail(order.email)
        val newAmount = order.exchangeAmount + amount
        if (newAmount < 0) {
            throw ArithmeticException("Amount is negative")
        }

        order.exchangeAmount = newAmount
        orderRepository.save(order)
    }

    @Transactional
    fun setType(orderId: Long, type: String) {
        val order = getEntityById(orderId)
        authContext.requireEmail(order.email)
        order.type = type
        orderRepository.save(order)
    }

    @Transactional
    fun delete(id: Long) {
        val order = getEntityById(id)
        authContext.requireEmail(order.email)
        orderRepository.delete(order)
    }

    @Transactional
    fun deleteByEmail(email: String) {
        orderRepository.deleteAllByEmail(email)
    }

    @Transactional
    fun reassignUserEmail(oldEmail: String, newEmail: String) {
        orderRepository.findAllByEmail(oldEmail).forEach { order ->
            order.email = newEmail
            orderRepository.save(order)
        }
    }

    @Transactional
    fun createMobileOrder(
        email: String,
        currencyId: String,
        currencyName: String,
        type: String,
        amount: Double,
        price: Double,
        changeCurrency: String = "USD",
    ): OrderEntity =
        orderRepository.save(
            OrderEntity(
                currencyId = currencyId,
                currencyName = currencyName,
                amount = amount,
                exchangeCurrencyId = changeCurrency,
                exchangeAmount = price,
                type = type,
                status = OrderStatus.STARTED,
                email = email,
                executedAt = LocalDateTime.now(),
            ),
        )

    fun getOrderEntitiesForCurrentUser(): List<OrderEntity> =
        orderRepository.findAllByEmail(authContext.currentUserEmail())

    @Scheduled(fixedRate = 6000)
    @Transactional
    fun processOrderStatus() {
        val orders = orderRepository.findTop10ByStatusInAndExecutedAtLessThanEqualOrderByExecutedAtAsc(
            listOf(OrderStatus.STARTED, OrderStatus.PROCESSING),
            LocalDateTime.now(),
        )

        orders.forEach { order ->
            when (order.status) {
                OrderStatus.STARTED -> {
                    order.status = OrderStatus.PROCESSING
                    order.executedAt = LocalDateTime.now()
                }

                OrderStatus.PROCESSING -> {
                    order.status = if (random.nextDouble() < 0.9 && applyCompletedOrderEffects(order)) {
                        OrderStatus.COMPLETED
                    } else {
                        OrderStatus.CANCELLED
                    }
                    order.finishedAt = LocalDateTime.now()
                    order.executedAt = null
                }

                OrderStatus.COMPLETED, OrderStatus.CANCELLED -> Unit
            }

            orderRepository.save(order)
        }
    }

    private fun applyCompletedOrderEffects(order: OrderEntity): Boolean {
        val user = userRepository.findByEmail(order.email)
            ?: throw EntityNotFoundException("User not found")
        val userCoin = userCoinRepository.findByCurrencyIdAndUser(order.currencyId, user)

        return if (order.type.equals("buy", ignoreCase = true)) {
            if (user.balance < order.exchangeAmount) {
                false
            } else {
                user.balance -= order.exchangeAmount
                val targetUserCoin = userCoin ?: ru.vsu.servicesback.entity.UserCoinEntity(
                    currencyId = order.currencyId,
                    name = order.currencyName,
                    amount = 0.0,
                    user = user,
                )
                targetUserCoin.amount += order.amount
                userRepository.save(user)
                userCoinRepository.save(targetUserCoin)
                true
            }
        } else {
            val targetUserCoin = userCoin ?: return false
            if (targetUserCoin.amount < order.amount) {
                false
            } else {
                targetUserCoin.amount -= order.amount
                user.balance += order.exchangeAmount
                userCoinRepository.save(targetUserCoin)
                userRepository.save(user)
                true
            }
        }
    }

    private fun getEntityById(orderId: Long): OrderEntity =
        orderRepository.findById(orderId)
            .orElseThrow { EntityNotFoundException("Order not found") }

    private fun OrderEntity.toResponse(): OrderResponse =
        OrderResponse(
            id = requireNotNull(id),
            currencyId = currencyId,
            amount = amount,
            exchangeCurrencyId = exchangeCurrencyId,
            exchangeAmount = exchangeAmount,
            type = type,
            status = status,
            email = email,
            timestamp = finishedAt ?: executedAt,
        )
}
