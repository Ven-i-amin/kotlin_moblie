package ru.vsu.servicesback.dto.request

import jakarta.validation.constraints.NotNull

data class BalanceChangeRequest(
    @field:NotNull
    val deltaBalance: Double,
)
