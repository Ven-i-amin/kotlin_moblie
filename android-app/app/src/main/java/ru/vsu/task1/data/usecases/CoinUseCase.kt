package ru.vsu.task1.data.usecases

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vsu.task1.data.repositories.coininfo.CoinInfoRepository
import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.data.repositories.watchlist.WatchlistRepository

class CoinUseCase(
    private val repository: CoinInfoRepository,
    private val watchlistRepository: WatchlistRepository,
    private val authUseCase: AuthUseCase
) {
    private val _mainCoinInfo = MutableStateFlow<List<CoinInfo>>(emptyList())
    val mainCoinInfo = _mainCoinInfo.asStateFlow()

    suspend fun getWatchlist(): List<CoinInfo>? {
        if (!authUseCase.checkUserToken()) return null

        val watchlistResponse = watchlistRepository.getWatchlist(
            authUseCase.userToken.value!!
        )

        val coinInfoResponse = getCoinList()

        return watchlistResponse
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

    suspend fun setCoinList() {
        val changeCurrencySymbol = authUseCase.changeCurrencySymbol.value
        val symbol = fromGeckoToBitgetSymbol(changeCurrencySymbol)



        _mainCoinInfo.value = repository.getCoinList().map {
            it.copy(bitgetSymbol = it.symbol.uppercase() + symbol)
        }
    }

    suspend fun getCoinInfo(id: String): CoinInfo? {
        if (mainCoinInfo.value.isEmpty()) setCoinList()

        val coinInfo = mainCoinInfo.value.find { it.id == id }

        if (coinInfo == null) {
            return null
        }

        val a = repository.getMarketCoinInfo(coinInfo)

        return a
    }

    suspend fun getCoinList(): List<CoinInfo> {
        if (mainCoinInfo.value.isEmpty()) setCoinList()

        return repository.getMarketCoinList(mainCoinInfo.value)
    }

    private fun fromGeckoToBitgetSymbol(vsCurrency: String): String {
        if (vsCurrency.lowercase() == "usd") {
            return (vsCurrency + "t").uppercase()
        }

        return vsCurrency.uppercase()
    }
}