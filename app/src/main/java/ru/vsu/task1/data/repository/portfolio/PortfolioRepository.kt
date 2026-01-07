package ru.vsu.task1.data.repository.portfolio

import ru.vsu.task1.domain.models.home.UserCoin

interface PortfolioRepository {
    suspend fun getUserCoins() : List<UserCoin>
}