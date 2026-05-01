package ru.vsu.task1.data.repositories.transaction

import ru.vsu.task1.data.models.home.Transaction
import ru.vsu.task1.data.services.GatewayService

class TransactionRepositoryImpl(
    private val gatewayService: GatewayService
) : TransactionRepository {
    override suspend fun getUserTransactions(authToken: String): List<Transaction> {
        return gatewayService.getTransactions("Bearer $authToken")
    }
}
