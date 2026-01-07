package ru.vsu.task1.data.repository.home

import ru.vsu.task1.domain.models.home.Order
import ru.vsu.task1.domain.models.home.UserCoin
import ru.vsu.task1.domain.models.home.Transaction

interface HomeRepository {
    suspend fun getUserTransactions(authToken: String) : List<Transaction>
    suspend fun getUserCurrencies(authToken: String) : List<UserCoin>

    suspend fun getWatchlist(authToken: String) : List<String>

    suspend fun getOrders(authToken: String) : List<Order>
}