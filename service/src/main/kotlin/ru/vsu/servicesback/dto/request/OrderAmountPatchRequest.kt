package ru.vsu.servicesback.dto.request

import jakarta.validation.constraints.NotNull

data class OrderAmountPatchRequest(
    @field:NotNull
    val amount: Double,
)
