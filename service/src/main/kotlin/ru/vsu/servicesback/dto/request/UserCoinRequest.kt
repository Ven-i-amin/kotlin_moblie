package ru.vsu.servicesback.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero

data class UserCoinRequest(
    @field:NotBlank
    val currencyId: String,
    @field:NotBlank
    val name: String,
    @field:PositiveOrZero
    val amount: Double = 0.0,
    @field:NotBlank
    val ownerEmail: String,
)
