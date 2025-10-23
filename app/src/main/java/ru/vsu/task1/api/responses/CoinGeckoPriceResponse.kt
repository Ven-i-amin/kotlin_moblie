package ru.vsu.task1.api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinGeckoPriceResponse(
    @SerialName("prices")
    val prices: List<List<Double>>
)
