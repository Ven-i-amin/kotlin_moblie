package ru.vsu.task1.data.repositories.transaction

import android.util.Log
import ru.vsu.task1.data.models.home.Order
import ru.vsu.task1.data.models.home.UserCoin
import ru.vsu.task1.data.models.home.Transaction

class TransactionRepositoryImpl() : TransactionRepository {
    private var transactionList = mutableListOf(
        Transaction("1", "bitcoin", -10.0, "usd", 10000000.0, 1730843100000L),
        Transaction("2", "ethereum", 1.0, "usd", -5000.0, 1659195000000L),
        Transaction("3", "solana", 2.0, "usd", -1000.0, 1673338500000L)
    )

    override suspend fun getUserTransactions(authToken: String): List<Transaction> {
        Log.d("HomeRepositoryImpl", "getUserTransactions")
        return transactionList
    }
}