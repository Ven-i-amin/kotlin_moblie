package ru.vsu.task1.data.repository.trade

import ru.vsu.task1.domain.models.trade.CoinChart

interface TradeRepository {
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