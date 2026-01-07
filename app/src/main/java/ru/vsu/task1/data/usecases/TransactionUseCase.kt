package ru.vsu.task1.data.usecases

import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.data.models.home.Transaction
import ru.vsu.task1.data.repositories.transaction.TransactionRepository

class TransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val authUseCase: AuthUseCase,
    private val coinUseCase: CoinUseCase
) {
    suspend fun getTransactions(): Map<Transaction, CoinInfo>? {
        if (!authUseCase.checkUserToken()) return null

        val transactionResponse = transactionRepository.getUserTransactions(
            authUseCase.userToken.value!!
        )

        val coinListResponse = coinUseCase.getCoinList()

        return transactionResponse
            .associateWith { transaction ->
                coinListResponse.find {
                    it.id == transaction.currencyId
                }
            }
            .filterValues { it != null }
            .mapValues { it.value!! }
            .toSortedMap { o1, o2 -> -o1.timestamp.compareTo(o2.timestamp) }
    }
}