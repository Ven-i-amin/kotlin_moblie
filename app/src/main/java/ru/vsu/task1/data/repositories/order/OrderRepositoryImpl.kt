package ru.vsu.task1.data.repositories.order

import ru.vsu.task1.data.models.home.Order

class OrderRepositoryImpl : OrderRepository {
    private val orders = mutableListOf(
        Order(1, "bitcoin", "Bitcoin", "buy", 10.0, 10000.0, "open"),
        Order(2, currencyId = "bitcoin", currencyName = "Bitcoin", type = "sell", amount = 5.0, price = 12000.0, status = "open")
    )

    override suspend fun getOrders(authToken: String): List<Order> {
        return orders
    }

    override suspend fun addOrder(authToken: String, newOrder: Order): List<Order> {
        orders.add(Order(
            orders.size.toLong() + 1,
            newOrder.currencyId,
            newOrder.currencyName,
            newOrder.type,
            newOrder.amount,
            newOrder.price,
            "open"
        ))

        return orders
    }
}