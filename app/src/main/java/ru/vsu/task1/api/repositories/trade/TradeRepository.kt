package ru.vsu.task1.api.repositories.trade

import ru.vsu.task1.api.models.trade.MarketChart

interface TradeRepository {
    suspend fun getHistoryPrices(
        id: String,
        vsCurrency: String,
        days: String
    ): MarketChart
}