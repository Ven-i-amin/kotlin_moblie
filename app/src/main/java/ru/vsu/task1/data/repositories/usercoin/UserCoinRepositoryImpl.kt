package ru.vsu.task1.data.repositories.usercoin

import ru.vsu.task1.data.models.home.UserCoin

class UserCoinRepositoryImpl : UserCoinRepository {
    override suspend fun getUserCurrencies(authToken: String): List<UserCoin> {
        return listOf(
            UserCoin("bitcoin", "Bitcoin", 10.0),
            UserCoin("ethereum", "Ethereum", 1.0),
            UserCoin("solana", "Solana", 2.0)
        )
    }
}