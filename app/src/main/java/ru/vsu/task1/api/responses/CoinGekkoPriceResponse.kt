package ru.vsu.task1.api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinGekkoPriceResponse(
    @SerialName("prices")
    val prices: List<List<Double>>,
    @SerialName("market_caps")
    val marketCaps:  List<List<Double>>,

    @SerialName("total_volumes")
    val totalVolumes: List<List<Double>>
)
