package ru.vsu.task1.ui.screens.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.task1.domain.models.coin.CoinInfo
import ru.vsu.task1.data.repository.coin.CoinRepository
import ru.vsu.task1.data.repository.trade.TradeRepository
import ru.vsu.task1.domain.usecases.CoinUseCase
import java.time.Instant

class TradeViewModel(
    private val repository: TradeRepository,
    private val coinRepository: CoinRepository,
    private val coinUseCase: CoinUseCase
) : ViewModel() {
    // coin info
    private val _coinInfo = MutableStateFlow<CoinInfo?>(null)
    val coinInfo: StateFlow<CoinInfo?> = _coinInfo.asStateFlow()

    // chart prices
    private val _prices = MutableStateFlow<List<Float>>(emptyList())
    val prices: StateFlow<List<Float>> = _prices.asStateFlow()

    // loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isChartLoading = MutableStateFlow(false)
    val isChartLoading: StateFlow<Boolean> = _isChartLoading.asStateFlow()

    // error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchMarketChart(bitgetSymbol: String, days: String) {
        _isChartLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = repository.getMarketChart(
                    bitgetSymbol = bitgetSymbol,
                    granularity = days,
                    endTime = System.currentTimeMillis().toString()
                )

                _prices.value = response.data.map { it[4].toFloat() }
                _isChartLoading.value = false
            } catch (e: Exception) {
                _error.value = "Failed to load data: ${e.message}"
                e.printStackTrace()

            }
        }
    }

    fun fetchCoinInfo(coinId: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = coinUseCase.getCoinInfo(coinId)
                _coinInfo.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = "Failed to get coin info: ${e.message}"
                e.printStackTrace()

            }
        }
    }
}
