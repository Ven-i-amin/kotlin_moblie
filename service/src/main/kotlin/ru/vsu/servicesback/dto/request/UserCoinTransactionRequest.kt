package ru.vsu.servicesback.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class UserCoinTransactionRequest(
    @field:Email
    @field:NotBlank
    val oldUserEmail: String,
    @field:Email
    @field:NotBlank
    val newUserEmail: String,
    @field:NotBlank
    val currencyId: String,
    @field:Positive
    val changeAmount: Double,
)
