package ru.vsu.task1.api.viewmodels.trade

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.task1.api.repositories.trade.TradeRepository

class TradeScreenViewModel(
    private val repository: TradeRepository,
) : ViewModel() {
    val coinName = mutableStateOf<String?>(null)
    val coinImage = mutableStateOf<String?>(null)
    val coinChange24h = mutableStateOf<Double?>(null)
    val coinChangePercentage24h = mutableStateOf<Double?>(null)
    val prices = mutableStateOf<List<Float>>(emptyList())
    val isLoading = mutableStateOf(false)
    val isChartLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    init {
        fetchMarketChart("bitcoin", "usd", "1")
    }

    fun fetchMarketChart(coinId: String, currency: String, days: String) {
        isChartLoading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val response = repository.getHistoryPrices(
                    id = coinId,
                    vsCurrency = currency,
                    days = days
                )

                prices.value = response.prices.map{ it[1].toFloat() }
            } catch (e: Exception) {
                error.value = "Failed to load data: ${e.message}"
                e.printStackTrace()
            } finally {
                isChartLoading.value = false
            }
        }
    }

    fun fetchCoinInfo(coinId: String) {
        isLoading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val response = repository.getCoinInfo(coinId)

                response.let {
                    coinName.value = it.name
                    coinImage.value = it.image?.small
                    coinChange24h.value = it.marketData?.priceChange24h
                    coinChangePercentage24h.value = it.marketData?.priceChangePercentage24h
                }
            } catch (e: Exception) {
                error.value = "Failed to get coin info: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }
}