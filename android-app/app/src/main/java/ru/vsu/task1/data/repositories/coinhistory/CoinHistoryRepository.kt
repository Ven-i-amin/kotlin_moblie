package ru.vsu.task1.data.repositories.coinhistory

import ru.vsu.task1.data.models.trade.CoinChart

interface CoinHistoryRepository {
    suspend fun getMarketChart(
        bitgetSymbol: String,
        granularity: String,
        endTime: String
    ): CoinChart
    suspend fun getMarketChart(
        bitgetSymbol: String,
        granularity: String,
        endTime: String,
        limit: String
    ): CoinChart
}