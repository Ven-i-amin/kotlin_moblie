package ru.vsu.servicesback.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class OrderRequest(
    @field:NotBlank
    val currencyId: String,
    @field:Positive
    val amount: Double,
    @field:NotBlank
    val exchangeCurrencyId: String,
    @field:Positive
    val exchangeAmount: Double,
    @field:NotBlank
    val type: String,
    @field:Email
    @field:NotBlank
    val email: String,
)
