package ru.vsu.task1.data.repositories.user

import ru.vsu.task1.data.models.backend.GatewayBalanceRequest
import ru.vsu.task1.data.models.backend.GatewayLoginRequest
import ru.vsu.task1.data.models.backend.GatewayRegisterRequest
import ru.vsu.task1.data.models.auth.User
import ru.vsu.task1.data.services.GatewayService

class UserRepositoryImpl(
    private val gatewayService: GatewayService
) : UserRepository {
    private fun authHeader(token: String): String = "Bearer $token"

    override suspend fun userLogin(email: String, password: String): String {
        return gatewayService.login(
            GatewayLoginRequest(email = email, password = password)
        )
    }

    override suspend fun userRegister(email: String, password: String, name: String): String {
        return gatewayService.register(
            GatewayRegisterRequest(
                email = email,
                password = password,
                name = name
            )
        )
    }

    override suspend fun userLogout(authToken: String) : String? {
        gatewayService.logout(authHeader(authToken))
        return null
    }


    override suspend fun getUserInfo(authToken: String): User {
        return gatewayService.getCurrentUser(authHeader(authToken))
    }

    override suspend fun setUserInfo(authToken: String, newData: User): User {
        return gatewayService.updateCurrentUser(
            authHeader(authToken),
            newData
        )
    }

    override suspend fun setBalance(authToken: String, newBalance: Double) {
        gatewayService.setBalance(
            authHeader(authToken),
            GatewayBalanceRequest(balance = newBalance)
        )
    }
}
