package ru.vsu.task1.data.usecases

import ru.vsu.task1.data.models.auth.User
import ru.vsu.task1.data.repositories.user.UserRepository

class UserUseCase(
    private val userRepository: UserRepository,
    private val authUseCase: AuthUseCase
) {
    suspend fun getUserInfo(token: String) : User? {
        if (!authUseCase.checkUserToken()) return null

        return userRepository.getUserInfo(token)
    }

    suspend fun setUserInfo(token: String, newUser: User) : User? {
        if (!authUseCase.checkUserToken()) return null

        return userRepository.setUserInfo(token, newUser)
    }

    suspend fun getUserBalance(): Double? {
        if (!authUseCase.checkUserToken()) return null

        return userRepository.getUserInfo(authUseCase.userToken.value!!).balance
    }

    suspend fun setUserBalance(newBalance: Double): Double? {
        if (!authUseCase.checkUserToken()) return null

        userRepository.setBalance(
            authUseCase.userToken.value!!,
            newBalance
        )

        return newBalance
    }
}