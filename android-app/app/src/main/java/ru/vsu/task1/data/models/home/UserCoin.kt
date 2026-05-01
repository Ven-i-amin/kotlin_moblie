package ru.vsu.task1.data.models.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCoin (
    @SerialName("currency_id")
    val currencyId: String,
    @SerialName("name")
    val name: String,
    @SerialName("amount")
    val amount: Double
)