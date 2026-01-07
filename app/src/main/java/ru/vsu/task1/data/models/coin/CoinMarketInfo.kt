package ru.vsu.task1.data.models.coin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinMarketInfo(
    val data: List<MarketData>
)

@Serializable
data class MarketData(
    val symbol: String,
    @SerialName("lastPr") val price: Double,
    @SerialName("open") val openPrice: Double,
    @SerialName("change24h") val priceChangeCoef24h: Double,
)