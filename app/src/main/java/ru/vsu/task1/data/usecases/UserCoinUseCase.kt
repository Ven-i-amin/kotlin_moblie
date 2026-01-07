package ru.vsu.task1.data.usecases

import ru.vsu.task1.data.models.home.UserCoin
import ru.vsu.task1.data.repositories.usercoin.UserCoinRepository

class UserCoinUseCase(
    private val userCoinRepository: UserCoinRepository,
    private val authUseCase: AuthUseCase,
) {

    suspend fun getUserCoins(): List<UserCoin>? {
        if (!authUseCase.checkUserToken()) return null

        return userCoinRepository.getUserCurrencies(authUseCase.userToken.value!!)
    }
}