package ru.vsu.task1.domain.usecases

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vsu.task1.data.repository.coin.CoinRepository
import ru.vsu.task1.domain.models.coin.CoinInfo

class CoinUseCase(val repository: CoinRepository, val profileUseCase: ProfileUseCase) {
    private val _mainCoinInfo = MutableStateFlow<List<CoinInfo>>(emptyList())
    val mainCoinInfo = _mainCoinInfo.asStateFlow()

    suspend fun setCoinList() {
        val changeCurrencySymbol = profileUseCase.changeCurrencySymbol.value
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