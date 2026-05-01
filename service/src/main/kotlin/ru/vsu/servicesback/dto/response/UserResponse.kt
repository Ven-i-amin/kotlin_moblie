package ru.vsu.servicesback.dto.response

data class UserResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val password: String,
    val balance: Double,
    val token: String? = null,
)
