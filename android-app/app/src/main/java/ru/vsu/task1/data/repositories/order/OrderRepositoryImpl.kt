package ru.vsu.task1.data.repositories.order

import ru.vsu.task1.data.models.backend.GatewayOrderCreateRequest
import ru.vsu.task1.data.models.home.Order
import ru.vsu.task1.data.services.GatewayService

class OrderRepositoryImpl(
    private val gatewayService: GatewayService
) : OrderRepository {

    override suspend fun getOrders(authToken: String): List<Order> {
        return gatewayService.getOrders("Bearer $authToken")
    }

    override suspend fun addOrder(authToken: String, newOrder: Order): List<Order> {
        return gatewayService.createOrder(
            "Bearer $authToken",
            GatewayOrderCreateRequest(
                currencyId = newOrder.currencyId,
                currencyName = newOrder.currencyName,
                type = newOrder.type,
                amount = newOrder.amount,
                price = newOrder.price
            )
        )
    }
}
