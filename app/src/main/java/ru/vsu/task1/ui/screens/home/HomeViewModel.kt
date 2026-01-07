package ru.vsu.task1.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.task1.data.repository.auth.AuthRepository
import ru.vsu.task1.data.repository.home.HomeRepository
import ru.vsu.task1.domain.models.coin.CoinInfo
import ru.vsu.task1.domain.models.home.Transaction
import ru.vsu.task1.domain.usecases.CoinUseCase
import ru.vsu.task1.domain.usecases.ProfileUseCase
import ru.vsu.task1.domain.usecases.UserCoinUseCase

class HomeViewModel(
    private val repository: HomeRepository,
    private val loginRepository: AuthRepository,
    private val userUseCase: ProfileUseCase,
    private val coinUseCase: CoinUseCase,
    private val userCoinUseCase: UserCoinUseCase
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
                _transactions.value = userCoinUseCase.getTransactions()

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
                _watchlistCoins.value = userCoinUseCase.getWatchlist()

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