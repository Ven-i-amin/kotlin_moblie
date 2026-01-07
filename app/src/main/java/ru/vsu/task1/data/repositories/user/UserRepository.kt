package ru.vsu.task1.data.repositories.user

import ru.vsu.task1.data.models.auth.User

interface UserRepository {
    suspend fun userLogin(email: String, password: String) : String
    suspend fun userRegister(email: String, password: String, name: String) : String
    suspend fun userLogout(authToken: String) : String?
    suspend fun getUserInfo(authToken: String) : User

    suspend fun setUserInfo(authToken: String, newData: User) : User
    suspend fun setBalance(authToken: String, newBalance: Double)
}