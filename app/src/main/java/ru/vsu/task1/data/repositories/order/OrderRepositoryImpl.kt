package ru.vsu.task1.data.repositories.order

import ru.vsu.task1.data.models.home.Order

class OrderRepositoryImpl : OrderRepository {
    override suspend fun getOrders(authToken: String): List<Order> {
        return listOf(
            Order(1, "bitcoin", "Bitcoin", "buy", 10.0, 10000.0, "open"),
            Order(2, currencyId = "bitcoin", currencyName = "Bitcoin", type = "sell", amount = 5.0, price = 12000.0, status = "open")
        )
    }
}