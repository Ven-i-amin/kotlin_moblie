package ru.vsu.task1.data.repository.trade

import ru.vsu.task1.domain.models.trade.MarketChart

interface TradeRepository {
    suspend fun getHistoryPrices(
        id: String,
        vsCurrency: String,
        days: String
    ): MarketChart
}