package ru.vsu.servicesback.dto.response

data class UserAuthResponse(
    val token: String,
    val user: UserResponse,
)
