package ru.vsu.task1.data.repositories.coinhistory

import android.util.Log
import ru.vsu.task1.data.services.BitgetService
import ru.vsu.task1.data.models.trade.CoinChart

class CoinHistoryRepositoryImpl(private val service: BitgetService) : CoinHistoryRepository {
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