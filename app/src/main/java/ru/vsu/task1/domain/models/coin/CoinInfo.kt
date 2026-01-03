package ru.vsu.task1.domain.models.coin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinInfo(
    @SerialName("id") val id: String,
    @SerialName("symbol") val symbol: String,
    var bitgetSymbol: String = "",
    @SerialName("name") val name: String,
    @SerialName("image") val image: String? = null,
    @SerialName("current_price") var currentPrice: Double? = null,
    @SerialName("price_change_24h") var priceChange24h: Double? = null,
    @SerialName("price_change_percentage_24h") var priceChangePercentage24h: Double? = null,
)
