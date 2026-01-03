package ru.vsu.task1.data.repository.trade

import android.util.Log
import ru.vsu.task1.data.services.BitgetService
import ru.vsu.task1.domain.models.trade.CoinChart

class TradeRepositoryImpl(private val service: BitgetService) : TradeRepository {
    override suspend fun getMarketChart(
        bitgetSymbol: String,
        granularity: String,
        endTime: String
    ): CoinChart {
        val response = service.getMarketChartInfo(
            symbol = bitgetSymbol,
            granularity = granularity,
            endTime = endTime
        )

        Log.d("MarketChart", "Info")

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            Log.e("TradeRepositoryImpl", "Code ${response.code()}.\n ${response.message()}")
            throw Exception(response.message())
        }
    }

    override suspend fun getMarketChart(
        bitgetSymbol: String,
        granularity: String,
        endTime: String,
        limit: String
    ): CoinChart {
        val response = service.getMarketChartInfo(
            symbol = bitgetSymbol,
            granularity = granularity,
            endTime = endTime,
            limit = limit
        )

        Log.d("MarketChart", "Info")

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            Log.e("TradeRepositoryImpl", response.message())
            throw Exception(response.message())
        }
    }
}