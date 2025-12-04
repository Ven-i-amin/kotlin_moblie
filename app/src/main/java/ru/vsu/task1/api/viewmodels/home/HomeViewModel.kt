package ru.vsu.task1.api.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.task1.api.domain.UserUseCase
import ru.vsu.task1.api.repositories.home.HomeRepository
import ru.vsu.task1.api.models.home.Transaction
import ru.vsu.task1.api.models.trade.CoinInfo
import ru.vsu.task1.api.repositories.generic.CoinRepository
import ru.vsu.task1.api.repositories.login.LoginRepository

class HomeViewModel(
    private val repository: HomeRepository,
    private val coinRepository: CoinRepository,
    private val loginRepository: LoginRepository,
    private val userUseCase: UserUseCase,
) : ViewModel() {
    // loading
    private val _isLoadingBalance = MutableStateFlow(false)
    val isLoadingBalance: StateFlow<Boolean> = _isLoadingBalance.asStateFlow()

    private val _isLoadingCoins = MutableStateFlow(false)
    val isLoadingCoins: StateFlow<Boolean> = _isLoadingCoins.asStateFlow()

    private val _isLoadingTransactions = MutableStateFlow(false)
    val isLoadingTransactions: StateFlow<Boolean> = _isLoadingTransactions.asStateFlow()

    // error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // transactions
    private val _transactions = MutableStateFlow<Map<Transaction, CoinInfo>>(emptyMap())
    val transactions: StateFlow<Map<Transaction, CoinInfo>> = _transactions.asStateFlow()

    // coins
    private val _watchlistCoins = MutableStateFlow<List<CoinInfo>>(emptyList())
    val watchlistCoins: StateFlow<List<CoinInfo>> = _watchlistCoins.asStateFlow()

    //balance
    private val _balance = MutableStateFlow<Double?>(null)
    val balance: StateFlow<Double?> = _balance.asStateFlow()

    fun fetchUserTransactions() {
        _isLoadingTransactions.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = repository.getUserTransactions(
                    userUseCase.userToken.value ?: "token"
                )

                _transactions.value = response
                    .associateWith { coinRepository.getCoinInfo(it.currencyName) }
                    .toSortedMap { o1, o2 -> -o1.timestamp.compareTo(o2.timestamp) }

                _isLoadingTransactions.value = false
            } catch (_: Exception) {
                _error.value = "Failed to fetch transactions"
            }
        }
    }

    fun fetchWatchlist() {
        _isLoadingCoins.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = loginRepository.getUserInfo(
                    userUseCase.userToken.value ?: "token"
                )

                _watchlistCoins.value = response
                    .watchlist
                    .map {
                        coinRepository.getCoinInfo(it)
                    }

                _isLoadingCoins.value = false
            } catch (_: Exception) {
                _error.value = "Failed to fetch coins"
            }
        }
    }

    fun fetchBalance() {
        _isLoadingBalance.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = loginRepository.getUserInfo(
                    userUseCase.userToken.value ?: "token"
                )

                _balance.value = response.balance

                _isLoadingBalance.value = false
            } catch (e: Exception) {
                _error.value = "Failed to fetch balance"
            }
        }
    }
}