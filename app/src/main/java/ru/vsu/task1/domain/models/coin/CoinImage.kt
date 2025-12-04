package ru.vsu.task1.domain.models.coin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinImage(
    @SerialName("thumb") val thumb: String? = null,
    @SerialName("small") val small: String? = null,
    @SerialName("large") val large: String? = null
)
