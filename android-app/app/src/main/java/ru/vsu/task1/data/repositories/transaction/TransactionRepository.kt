package ru.vsu.task1.data.repositories.transaction

import ru.vsu.task1.data.models.home.Transaction

interface TransactionRepository {
    suspend fun getUserTransactions(authToken: String) : List<Transaction>
}