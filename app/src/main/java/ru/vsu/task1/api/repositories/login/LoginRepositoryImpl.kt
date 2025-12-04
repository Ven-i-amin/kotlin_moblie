package ru.vsu.task1.api.repositories.login

import ru.vsu.task1.api.models.auth.User

class LoginRepositoryImpl() : LoginRepository {
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