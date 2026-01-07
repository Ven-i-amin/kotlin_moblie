package ru.vsu.task1.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.task1.data.repositories.user.UserRepository
import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.data.models.home.Transaction
import ru.vsu.task1.data.usecases.CoinUseCase
import ru.vsu.task1.data.usecases.TransactionUseCase
import ru.vsu.task1.data.usecases.AuthUseCase

class HomeViewModel(
    private val loginRepository: UserRepository,
    private val authUseCase: AuthUseCase,
    private val transactionUseCase: TransactionUseCase,
    private val coinUseCase: CoinUseCase,
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
                val transactions = transactionUseCase.getTransactions()

                if (transactions == null) throw Exception()

                _transactions.value = transactions

                _isLoadingTransactions.value = false
            } catch (e: Exception) {
                _error.value = "Failed to fetch transactions"
                e.printStackTrace()
            }
        }
    }

    fun fetchWatchlist() {
        _isLoadingCoins.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val watchlist = coinUseCase.getWatchlist()

                if (watchlist == null) throw Exception()

                _watchlistCoins.value = watchlist

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
                if (!authUseCase.checkUserToken()) {
                    throw Exception()
                }

                val response = loginRepository.getUserInfo(
                    authUseCase.userToken.value!!
                )

                _balance.value = response.balance

                _isLoadingBalance.value = false
            } catch (e: Exception) {
                _error.value = "Failed to fetch balance"
                e.printStackTrace()
            }
        }
    }
}