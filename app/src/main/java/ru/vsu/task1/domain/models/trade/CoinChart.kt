package ru.vsu.task1.domain.models.trade

import kotlinx.serialization.Serializable

@Serializable
data class CoinChart(
    val data: List<List<Double>>
)
