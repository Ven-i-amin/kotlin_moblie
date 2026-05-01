package ru.vsu.servicesback.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class UserRequest(
    @field:NotBlank
    val fullName: String,
    @field:Email
    @field:NotBlank
    val email: String,
    @field:NotBlank
    val password: String,
    @field:Min(0)
    val balance: Double = 0.0,
)
