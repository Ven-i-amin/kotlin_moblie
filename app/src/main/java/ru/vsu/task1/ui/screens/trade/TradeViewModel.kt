package ru.vsu.task1.ui.screens.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.data.models.home.Order
import ru.vsu.task1.data.models.home.Transaction
import ru.vsu.task1.data.repositories.coinhistory.CoinHistoryRepository
import ru.vsu.task1.data.repositories.order.OrderRepository
import ru.vsu.task1.data.repositories.watchlist.WatchlistRepository
import ru.vsu.task1.data.usecases.CoinUseCase
import ru.vsu.task1.data.usecases.TransactionUseCase
import ru.vsu.task1.data.usecases.UserCoinUseCase
import ru.vsu.task1.data.usecases.AuthUseCase
import ru.vsu.task1.data.usecases.UserUseCase
import kotlin.math.max

class TradeViewModel(
    private val repository: CoinHistoryRepository,
    private val orderRepository: OrderRepository,
    private val watchlistRepository: WatchlistRepository,
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase,
    private val coinUseCase: CoinUseCase,
    private val transactionUseCase: TransactionUseCase,
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

    private val _isChosen = MutableStateFlow(false)
    val isChosen = _isChosen.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _isTradeSheetVisible = MutableStateFlow(false)
    val isTradeSheetVisible = _isTradeSheetVisible.asStateFlow()

    private val _coinAmount = MutableStateFlow<Double>(0.0)
    val userCoin = _coinAmount.asStateFlow()

    private val _userBalance = MutableStateFlow(0.0)
    val userBalance = _userBalance.asStateFlow()


    // loading
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isChartLoading = MutableStateFlow(true)
    val isChartLoading: StateFlow<Boolean> = _isChartLoading.asStateFlow()

    private val _loadingTransactions = MutableStateFlow(true)
    val loadingTransactions = _loadingTransactions.asStateFlow()

    private val _loadingOrders = MutableStateFlow(true)
    val loadingOrders = _loadingOrders.asStateFlow()

    private val _loadingTradeSheet = MutableStateFlow(true)
    val loadingTradeSheet = _loadingTradeSheet.asStateFlow()


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
                val transactions = transactionUseCase.getTransactions()

                if (transactions == null) throw Exception()

                _transactions.value = transactions.filter {
                    it.key.currencyId == coinInfo.value?.id
                }

                _loadingTransactions.value = false
            } catch (e: Exception) {
                _error.value = "Failed to load transactions"
                e.printStackTrace()
            }
        }
    }

    fun reverseWatchlist() {
        _isChosen.value = !_isChosen.value
    }

    fun fetchWatchlist() {
        viewModelScope.launch {
            try {
                val watchList = coinUseCase.getWatchlist()

                if (watchList == null) throw Exception()

                _isChosen.value = watchList
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
                if (authUseCase.userToken.value == null) {
                    throw Exception()

                }

                val orders = orderRepository.getOrders(
                    authUseCase.userToken.value!!
                )

                if (orders == null) throw Exception()


                _orders.value = orders.filter {
                    it.currencyId == coinInfo.value?.id
                }

                _loadingOrders.value = false
            } catch (e: Exception) {
                _error.value = "Failed to load orders"
                e.printStackTrace()
            }
        }
    }

    fun showTradeSheet() {
        _isTradeSheetVisible.value = true
    }

    fun hideTradeSheet() {
        _isTradeSheetVisible.value = false
    }

    fun fetchUserCoins() {
        _loadingTradeSheet.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val userCoins = userCoinUseCase.getUserCoins()

                if (userCoins == null) throw Exception()

                val response = userCoins.find {
                    it.currencyId == coinInfo.value?.id
                }

                fetchUserBalance()

                _coinAmount.value = response?.amount ?: 0.0

                _loadingTradeSheet.value = false
            } catch (e: Exception) {
                _error.value = "Failed to load user coins"
                e.printStackTrace()
            }
        }
    }

    private suspend fun fetchUserBalance() {
        val balance = userUseCase.getUserBalance()

        if (balance == null) throw Exception()

        _userBalance.value = max(balance, 0.0)
    }

    fun addNewOrder(
        type: String,
        price: Double,
        amount: Double
    ) {
        viewModelScope.launch {
            try {
                orderRepository.addOrder(
                    authUseCase.userToken.value!!,
                    Order (
                        id = 0L,
                        currencyId = coinInfo.value?.id!!,
                        currencyName = coinInfo.value?.name ?: "",
                        type = type,
                        amount = amount,
                        price = price,
                        status = "start"
                    )
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addCoinToWatchlist(coinId: String) {
        viewModelScope.launch {
            try {
                if (!authUseCase.checkUserToken()) {
                    throw Exception()
                }

                watchlistRepository.addCoinToWatchlist(
                    authUseCase.userToken.value!!,
                    coinId
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeCoinFromWatchlist(coinId: String) {
        viewModelScope.launch {
            try {
                if (!authUseCase.checkUserToken()) {
                    throw Exception()
                }

                watchlistRepository.removeCoinFromWatchlist(
                    authUseCase.userToken.value!!,
                    coinId
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
