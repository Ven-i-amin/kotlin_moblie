package ru.vsu.task1.data.repositories.user

import ru.vsu.task1.data.models.auth.User

class UserRepositoryImpl() : UserRepository {
    private var user = User(
        id = 1,
        fullName = "Name",
        email = "mail@mail.com",
        password = "password",
        balance = 1000.0
    )

    override suspend fun userLogin(email: String, password: String): String {
        return "token"
    }

    override suspend fun userRegister(email: String, password: String, name: String): String {
        return "token"
    }

    override suspend fun userLogout(authToken: String) : String? {
        return null
    }


    override suspend fun getUserInfo(authToken: String): User {
        return user
    }

    override suspend fun setUserInfo(authToken: String, newData: User): User {
        val newUser = User(
            id = user.id,
            fullName = newData.fullName,
            email = user.email,
            password = user.password,
            balance = newData.balance
        )

        user = newUser

        return newUser
    }

    override suspend fun setBalance(authToken: String, newBalance: Double) {
        val newUser = User(
            id = user.id,
            fullName = user.fullName,
            email = user.email,
            password = user.password,
            balance = newBalance
        )

        user = newUser
    }
}