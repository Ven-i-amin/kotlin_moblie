package ru.vsu.task1.api.repositories.home

import ru.vsu.task1.api.models.home.UserCoin
import ru.vsu.task1.api.models.home.Transaction

interface HomeRepository {
    suspend fun getUserTransactions(authToken: String) : List<Transaction>
    suspend fun getUserCurrencies(authToken: String) : List<UserCoin>

    suspend fun getWatchlist(authToken: String) : List<String>
}