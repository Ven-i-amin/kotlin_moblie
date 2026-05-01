package ru.vsu.task1.data.models.trade

import kotlinx.serialization.Serializable

@Serializable
data class CoinChart(
    val data: List<List<Double>>
)
