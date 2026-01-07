package ru.vsu.task1.ui.screens.coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.task1.data.repositories.coinhistory.CoinHistoryRepository
import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.data.usecases.CoinUseCase
import kotlin.math.min

private const val COIN_LIST_PAGE = 20

private const val TOP_BY_PERCENTAGE_NUM = 3

class CoinsViewModel(
    val coinHistoryRepository: CoinHistoryRepository,
    val coinUseCase: CoinUseCase
) : ViewModel() {
    private val _coins = MutableStateFlow<List<CoinInfo>>(emptyList())
    val coins = _coins.asStateFlow()

    private val _coinsSubList = MutableStateFlow<MutableList<CoinInfo>>(mutableListOf())
    val coinsSubList = _coinsSubList.asStateFlow()

    private val _subListLen = MutableStateFlow(0)
    val subListLen = _subListLen.asStateFlow()

    private val _topPercentageChange24h = MutableStateFlow<Map<CoinInfo, List<Float>>>(emptyMap())
    val topPercentageChange24h = _topPercentageChange24h.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val _loadingTop = MutableStateFlow(true)
    val loadingTop = _loadingTop.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _errorTop = MutableStateFlow<String?>(null)
    val errorTop = _errorTop.asStateFlow()

    fun fetchCoins() {
        _loading.value = true
        _error.value = null

        _loadingTop.value = true
        _errorTop.value = null

        viewModelScope.launch {
            try {
                val coins = coinUseCase.getCoinList()
                _coins.value = coins
                setSubList()
                setTopPercentageChange24h()
                _loading.value = false
            } catch (e: Exception) {
                _error.value = "Failed to load coins"
                e.printStackTrace()
            }
        }
    }

    fun setSubList() {
        _coinsSubList.value.addAll(
            _coins.value.subList(
                0,
                min(
                    COIN_LIST_PAGE,
                    _coins.value.size
                )
            )
        )
    }

    fun addSubList() {
        _subListLen.value += COIN_LIST_PAGE
        if (_subListLen.value < _coins.value.size) {
            _coinsSubList.value.addAll(
                _coins.value.subList(
                    _subListLen.value,
                    min(
                        _subListLen.value + COIN_LIST_PAGE,
                        _coins.value.size
                    )
                )
            )
        }
    }

    fun setTopPercentageChange24h() {
        viewModelScope.launch {
            try {
                _topPercentageChange24h.value = _coins
                    .value
                    .sortedByDescending {
                        it.priceChangePercentage24h
                    }
                    .subList(
                        0,
                        min(
                            TOP_BY_PERCENTAGE_NUM,
                            _coins.value.size
                        )
                    ).associateWith { coinChart ->
                        coinHistoryRepository.getMarketChart(
                            bitgetSymbol = coinChart.bitgetSymbol,
                            granularity = "15min",
                            endTime = System.currentTimeMillis().toString(),
                            limit = "30"
                        ).data.map {
                            it[4].toFloat()
                        }
                    }

                _loadingTop.value = false
            } catch (e: Exception) {
                _errorTop.value = "Failed to load top coins"
                e.printStackTrace()
            }
        }
    }
}