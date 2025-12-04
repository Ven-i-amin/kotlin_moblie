package ru.vsu.task1.data.repository.auth

import ru.vsu.task1.domain.models.auth.User

class AuthRepositoryImpl() : AuthRepository {
    override suspend fun userLogin(email: String, password: String): String {
        return "token"
    }

    override suspend fun userRegister(email: String, password: String, name: String): String {
        return "token"
    }

    override suspend fun getUserInfo(authToken: String): User {
        return User(
            id = 1,
            fullName = "Name",
            email = "mail@mail.com",
            password = "password", balance = 1000.0,
            watchlist = listOf("bitcoin", "ethereum", "solana")
        )
    }
}