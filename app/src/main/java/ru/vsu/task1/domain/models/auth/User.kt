package ru.vsu.task1.domain.models.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User (
    @SerialName("id")
    val id: Int,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("balance")
    val balance: Double,
    @SerialName("watchlist")
    val watchlist: List<String> = emptyList()
)