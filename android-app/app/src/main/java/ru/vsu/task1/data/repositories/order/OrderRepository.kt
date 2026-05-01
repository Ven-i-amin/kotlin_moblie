package ru.vsu.task1.data.repositories.order

import ru.vsu.task1.data.models.home.Order

interface OrderRepository {
    suspend fun getOrders(authToken: String) : List<Order>
    suspend fun addOrder(authToken: String, newOrder: Order) : List<Order>
}