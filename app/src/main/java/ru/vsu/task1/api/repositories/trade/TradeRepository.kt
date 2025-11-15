package ru.vsu.task1.api.repositories.trade

import ru.vsu.task1.api.responses.CoinInfoResponse
import ru.vsu.task1.api.responses.PriceResponse

interface TradeRepository {
    suspend fun getHistoryPrices(
        id: String,
        vsCurrency: String,
        days: String
    ): PriceResponse

    suspend fun getCoinInfo(id: String): CoinInfoResponse
}