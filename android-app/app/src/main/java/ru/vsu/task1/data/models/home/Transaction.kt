package ru.vsu.task1.data.models.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    @SerialName("id")
    val id: String,
    @SerialName("currency_id")
    val currencyId: String,
    @SerialName("currency_amount")
    val currencyAmount: Double,
    @SerialName("change_currency")
    val changeCurrency: String,
    @SerialName("change_amount")
    val changeAmount: Double,
    @SerialName("timestamp")
    val timestamp: Long
)
