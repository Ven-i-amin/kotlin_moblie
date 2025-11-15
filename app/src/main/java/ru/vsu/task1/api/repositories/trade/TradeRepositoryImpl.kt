package ru.vsu.task1.api.repositories.trade

import ru.vsu.task1.api.responses.CoinInfoResponse
import ru.vsu.task1.api.responses.PriceResponse
import ru.vsu.task1.api.services.CoinGekkoService

class TradeRepositoryImpl(private val service: CoinGekkoService) : TradeRepository {
    override suspend fun getHistoryPrices(
        id: String,
        vsCurrency: String,
        days: String
    ): PriceResponse {
        return service.getPrices(
            id = id,
            vsCurrency = vsCurrency,
            days = days,
        )
    }

    override suspend fun getCoinInfo(id: String): CoinInfoResponse {
        return service.getCoinInfo(
            id = id
        )
    }
}