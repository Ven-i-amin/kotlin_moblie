package ru.vsu.task1.ui.screens.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.task1.data.repository.trade.TradeRepository
import ru.vsu.task1.domain.models.coin.CoinInfo
import ru.vsu.task1.domain.models.home.Order
import ru.vsu.task1.domain.models.home.Transaction
import ru.vsu.task1.domain.usecases.CoinUseCase
import ru.vsu.task1.domain.usecases.UserCoinUseCase

class TradeViewModel(
    private val repository: TradeRepository,
    private val coinUseCase: CoinUseCase,
    private val userCoinUseCase: UserCoinUseCase
) : ViewModel() {
    // coin info
    private val _coinInfo = MutableStateFlow<CoinInfo?>(null)
    val coinInfo: StateFlow<CoinInfo?> = _coinInfo.asStateFlow()

    // chart prices
    private val _prices = MutableStateFlow<List<Float>>(emptyList())
    val prices: StateFlow<List<Float>> = _prices.asStateFlow()

    private val _transactions = MutableStateFlow<Map<Transaction, CoinInfo>>(emptyMap())
    val transaction = _transactions.asStateFlow()

    private val _watchlistOn = MutableStateFlow(false)
    val watchlistOn = _watchlistOn.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    // loading
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isChartLoading = MutableStateFlow(true)
    val isChartLoading: StateFlow<Boolean> = _isChartLoading.asStateFlow()

    private val _loadingTransactions = MutableStateFlow(true)
    val loadingTransactions = _loadingTransactions.asStateFlow()

    private val _loadingOrders = MutableStateFlow(true)
    val loadingOrders = _loadingOrders.asStateFlow()

    // error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _chartError = MutableStateFlow<String?>(null)
    val chartError: StateFlow<String?> = _chartError.asStateFlow()


    fun fetchMarketChart(bitgetSymbol: String, timeGaps: String) {
        _chartError.value = null
        if (bitgetSymbol.isBlank()) {
            _isChartLoading.value = false
            return
        }

        _isChartLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getMarketChart(
                    bitgetSymbol = bitgetSymbol,
                    granularity = timeGaps,
                    endTime = System.currentTimeMillis().toString()
                )

                _prices.value = response.data.map { it[4].toFloat() }

                _isChartLoading.value = false
            } catch (e: Exception) {
                _chartError.value = "Failed to load data: ${e.message}"
                _isChartLoading.value = false
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

    fun fetchTransaction() {
        _loadingTransactions.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                _transactions.value = userCoinUseCase.getTransactions().filter {
                    it.key.currencyName == coinInfo.value?.id
                }

                _loadingTransactions.value = false
            } catch (e: Exception) {
                _error.value = "Failed to load transactions"
                e.printStackTrace()
            }
        }
    }

    fun fetchWatchlist() {
        viewModelScope.launch {
            try {
                _watchlistOn.value = userCoinUseCase
                    .getWatchlist()
                    .map { it.id }
                    .contains(_coinInfo.value?.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchOrders() {
        _loadingOrders.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                _orders.value = userCoinUseCase.getOrder().filter {
                    it.currencyId == coinInfo.value?.id
                }

                _loadingOrders.value = false
            } catch (e: Exception) {
                _error.value = "Failed to load orders"
                e.printStackTrace()
            }
        }
    }
}
