package ru.vsu.task1.ui.screens.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.task1.data.repository.portfolio.PortfolioRepository
import ru.vsu.task1.domain.models.coin.CoinInfo
import ru.vsu.task1.domain.models.home.UserCoin
import ru.vsu.task1.domain.usecases.CoinUseCase

class PortfolioViewModel(
    private val portfolioRepository: PortfolioRepository,
    private val coinUseCase: CoinUseCase
) : ViewModel() {
    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _userCoinsAndInfo = MutableStateFlow<List<Pair<UserCoin, CoinInfo?>>>(emptyList())
    val userCoinsAndInfo = _userCoinsAndInfo.asStateFlow()

    private val _coinBalance = MutableStateFlow<List<Pair<String, Double>>>(emptyList())
    val coinsBalances = _coinBalance.asStateFlow()

    fun getUserCoins() {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val repository = portfolioRepository.getUserCoins()

                _userCoinsAndInfo.value = repository.map {
                    it to coinUseCase.getCoinInfo(it.id)
                }

                getCoinsBalances()

                _loading.value = false
            } catch (e: Exception) {
                _error.value = "Failed to fetch user coins"
                e.printStackTrace()
            }
        }
    }

    private fun getCoinsBalances() {
        val coinsAmounts = _userCoinsAndInfo.value
            .map { it.second?.currentPrice?.times(it.first.amount) }

        val averageBalance = coinsAmounts.sumOf { it ?: 0.0 } / coinsAmounts.size

        _coinBalance.value = _userCoinsAndInfo.value
            .map {
                it.first.id to
                        (it.second?.currentPrice?.times(it.first.amount) ?: -averageBalance)
            }
    }
}