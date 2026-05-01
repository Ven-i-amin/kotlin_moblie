package ru.vsu.task1.data.models.backend

import kotlinx.serialization.Serializable

@Serializable
data class GatewayLoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class GatewayRegisterRequest(
    val email: String,
    val password: String,
    val name: String,
)

@Serializable
data class GatewayBalanceRequest(
    val balance: Double,
)

@Serializable
data class GatewayWatchlistRequest(
    val coinId: String,
)

@Serializable
data class GatewayOrderCreateRequest(
    val currencyId: String,
    val currencyName: String,
    val type: String,
    val amount: Double,
    val price: Double,
)
