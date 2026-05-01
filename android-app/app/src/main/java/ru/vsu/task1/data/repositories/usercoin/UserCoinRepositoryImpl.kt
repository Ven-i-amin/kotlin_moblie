package ru.vsu.task1.data.repositories.usercoin

import ru.vsu.task1.data.models.home.UserCoin
import ru.vsu.task1.data.services.GatewayService

class UserCoinRepositoryImpl(
    private val gatewayService: GatewayService
) : UserCoinRepository {
    override suspend fun getUserCurrencies(authToken: String): List<UserCoin> {
        return gatewayService.getUserCoins("Bearer $authToken")
    }
}
