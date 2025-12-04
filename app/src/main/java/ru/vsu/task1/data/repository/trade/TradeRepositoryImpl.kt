package ru.vsu.task1.data.repository.trade

import android.util.Log
import ru.vsu.task1.data.services.CoinService
import ru.vsu.task1.domain.models.trade.MarketChart

class TradeRepositoryImpl(private val service: CoinService) : TradeRepository {
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