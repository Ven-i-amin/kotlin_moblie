package ru.vsu.servicesback.dto.request

import jakarta.validation.constraints.NotBlank

data class OrderTypePatchRequest(
    @field:NotBlank
    val type: String,
)
