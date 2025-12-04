package ru.vsu.task1.api.repositories.login

import ru.vsu.task1.api.models.auth.User

interface LoginRepository {
    suspend fun userLogin(email: String, password: String) : String
    suspend fun userRegister(email: String, password: String, name: String) : String
    suspend fun getUserInfo(authToken: String) : User
}