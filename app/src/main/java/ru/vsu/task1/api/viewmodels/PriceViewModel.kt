package ru.vsu.task1.api.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import ru.vsu.task1.api.services.CoinGekkoService

class PriceViewModel : ViewModel() {

    val prices = mutableStateOf<List<Float>>(emptyList())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)
    private val apiKey = "REMOVED"
    private val service: CoinGekkoService = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com/api/v3/")
        .build()
        .create(CoinGekkoService::class.java)

    init {
        fetchMarketChart("bitcoin", "usdt", "1")
    }

    fun fetchMarketChart(coinId: String, currency: String, days: String) {
        isLoading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val response = service.getPrices(coinId, currency, days, apiKey)

                prices.value = response.prices.map{ it[1].toFloat() }

            } catch (e: Exception) {
                error.value = "Failed to load data: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }
}