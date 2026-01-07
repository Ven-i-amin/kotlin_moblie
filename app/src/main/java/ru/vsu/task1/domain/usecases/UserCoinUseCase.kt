package ru.vsu.task1.domain.usecases

import ru.vsu.task1.data.repository.auth.AuthRepository
import ru.vsu.task1.data.repository.home.HomeRepository
import ru.vsu.task1.domain.models.coin.CoinInfo
import ru.vsu.task1.domain.models.home.Order
import ru.vsu.task1.domain.models.home.Transaction

class UserCoinUseCase(
    val homeRepository: HomeRepository,
    val authRepository: AuthRepository,
    val userUseCase: ProfileUseCase,
    val coinUseCase: CoinUseCase
) {
    suspend fun getTransactions(): Map<Transaction, CoinInfo> {
        val transactionResponse = homeRepository.getUserTransactions(
            userUseCase.userToken.value ?: "token"
        )

        val coinListResponse = coinUseCase.getCoinList()

        return transactionResponse
            .associateWith { transaction ->
                coinListResponse.find {
                    it.id == transaction.currencyName
                }
            }
            .filterValues { it != null }
            .mapValues { it.value!! }
            .toSortedMap { o1, o2 -> -o1.timestamp.compareTo(o2.timestamp) }
    }

    suspend fun getWatchlist(): List<CoinInfo> {
        val watchlistResponse = authRepository.getUserInfo(
            userUseCase.userToken.value ?: "token"
        )

        val coinInfoResponse = coinUseCase.getCoinList()

        return watchlistResponse
            .watchlist
            .map { el ->
                val coinInfo = coinInfoResponse.find {
                    it.id == el
                }

                if (coinInfo == null) {
                    return@map CoinInfo(
                        id = el,
                        symbol = el,
                        name = el,
                    )
                }

                coinInfo
            }
    }

    suspend fun getOrder(): List<Order> {
        return homeRepository.getOrders(
            userUseCase.userToken.value ?: "token"
        )
    }
}