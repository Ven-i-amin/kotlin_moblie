package ru.vsu.servicesback.dto.response

data class UserCoinResponse(
    val id: Long,
    val currencyId: String,
    val name: String,
    val amount: Double,
    val ownerEmail: String,
)
