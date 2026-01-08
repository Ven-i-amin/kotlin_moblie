package ru.vsu.task1.data.models.coin

import kotlinx.serialization.Serializable

@Serializable
data class CoinName(
    val id: String,
    val symbol: String,
    val name: String,
)