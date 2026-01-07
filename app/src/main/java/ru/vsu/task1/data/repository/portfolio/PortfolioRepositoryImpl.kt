package ru.vsu.task1.data.repository.portfolio

import ru.vsu.task1.domain.models.home.UserCoin

class PortfolioRepositoryImpl : PortfolioRepository {
    override suspend fun getUserCoins(): List<UserCoin> {
        return listOf(
            UserCoin("bitcoin", "Bitcoin", 0.02),
            UserCoin("ethereum", "Ethereum", 1.0),
            UserCoin("solana", "Solana", 2.0)
        )
    }
}