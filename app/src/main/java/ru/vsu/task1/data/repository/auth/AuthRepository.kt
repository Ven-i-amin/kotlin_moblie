package ru.vsu.task1.data.repository.auth

import ru.vsu.task1.domain.models.auth.User

interface AuthRepository {
    suspend fun userLogin(email: String, password: String) : String
    suspend fun userRegister(email: String, password: String, name: String) : String
    suspend fun getUserInfo(authToken: String) : User
}