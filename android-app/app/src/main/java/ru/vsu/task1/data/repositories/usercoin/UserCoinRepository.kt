package ru.vsu.task1.data.repositories.usercoin

import ru.vsu.task1.data.models.home.UserCoin

interface UserCoinRepository {
    suspend fun getUserCurrencies(authToken: String) : List<UserCoin>
}