package ru.vsu.task1.api.repositories.home

import android.util.Log
import ru.vsu.task1.api.models.home.UserCoin
import ru.vsu.task1.api.models.home.Transaction

class HomeRepositoryImpl() : HomeRepository {
    override suspend fun getUserTransactions(authToken: String): List<Transaction> {
        Log.d("HomeRepositoryImpl", "getUserTransactions")
        return listOf(
            Transaction("1", "bitcoin", -10.0, "usd", 10000000.0, 1730843100000L),
            Transaction("2", "ethereum", 1.0, "usd", -5000.0, 1659195000000L),
            Transaction("3", "solana", 2.0, "usd", -1000.0, 1673338500000L)
        )
    }

    override suspend fun getUserCurrencies(authToken: String): List<UserCoin> {
        return listOf(
            UserCoin("1", "bitcoin", 10.0),
            UserCoin("2", "ethereum", 1.0),
            UserCoin("3", "solana", 2.0)
        )
    }

    override suspend fun getWatchlist(authToken: String): List<String> {
        return listOf(
            "bitcoin",
            "ethereum",
            "solana"
        )
    }
}