package ru.vsu.task1.api.repositories.trade

import android.util.Log
import ru.vsu.task1.api.models.trade.MarketChart
import ru.vsu.task1.api.services.CoinGeckoService

class TradeRepositoryImpl(private val service: CoinGeckoService) : TradeRepository {
    override suspend fun getHistoryPrices(
        id: String,
        vsCurrency: String,
        days: String
    ): MarketChart {
        val response = service.getPrices(
            id = id,
            vsCurrency = vsCurrency,
            days = days,
        )

        Log.d("MarketChart", "Info")

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception(response.message())
        }
    }
}