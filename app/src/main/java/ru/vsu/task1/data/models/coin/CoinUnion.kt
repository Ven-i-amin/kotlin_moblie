package ru.vsu.task1.data.models.coin

import kotlinx.serialization.Serializable

@Serializable
data class CoinUnion(
    val name: CoinName,
    val data: MarketData
)