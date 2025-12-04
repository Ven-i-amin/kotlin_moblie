package ru.vsu.task1.domain.models.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCoin (
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("amount")
    val amount: Double
)