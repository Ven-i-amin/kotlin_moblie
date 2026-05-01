package ru.vsu.task1.data.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.vsu.task1.data.models.auth.User
import ru.vsu.task1.data.models.backend.GatewayBalanceRequest
import ru.vsu.task1.data.models.backend.GatewayLoginRequest
import ru.vsu.task1.data.models.backend.GatewayOrderCreateRequest
import ru.vsu.task1.data.models.backend.GatewayRegisterRequest
import ru.vsu.task1.data.models.backend.GatewayWatchlistRequest
import ru.vsu.task1.data.models.home.Order
import ru.vsu.task1.data.models.home.Transaction
import ru.vsu.task1.data.models.home.UserCoin

interface GatewayService {
    @POST("/api/mobile/auth/login")
    suspend fun login(@Body request: GatewayLoginRequest): String

    @POST("/api/mobile/auth/register")
    suspend fun register(@Body request: GatewayRegisterRequest): String

    @POST("/api/mobile/auth/logout")
    suspend fun logout(@Header("Authorization") authHeader: String): Response<Unit>

    @GET("/api/mobile/users/me")
    suspend fun getCurrentUser(@Header("Authorization") authHeader: String): User

    @PUT("/api/mobile/users/me")
    suspend fun updateCurrentUser(
        @Header("Authorization") authHeader: String,
        @Body user: User,
    ): User

    @PATCH("/api/mobile/users/me/balance")
    suspend fun setBalance(
        @Header("Authorization") authHeader: String,
        @Body request: GatewayBalanceRequest,
    ): Response<Unit>

    @GET("/api/mobile/user-coins")
    suspend fun getUserCoins(@Header("Authorization") authHeader: String): List<UserCoin>

    @GET("/api/mobile/orders")
    suspend fun getOrders(@Header("Authorization") authHeader: String): List<Order>

    @POST("/api/mobile/orders")
    suspend fun createOrder(
        @Header("Authorization") authHeader: String,
        @Body request: GatewayOrderCreateRequest,
    ): List<Order>

    @GET("/api/mobile/transactions")
    suspend fun getTransactions(@Header("Authorization") authHeader: String): List<Transaction>

    @GET("/api/mobile/watchlist")
    suspend fun getWatchlist(@Header("Authorization") authHeader: String): List<String>

    @POST("/api/mobile/watchlist")
    suspend fun addToWatchlist(
        @Header("Authorization") authHeader: String,
        @Body request: GatewayWatchlistRequest,
    ): Response<Unit>

    @DELETE("/api/mobile/watchlist/{coinId}")
    suspend fun removeFromWatchlist(
        @Header("Authorization") authHeader: String,
        @Path("coinId") coinId: String,
    ): Response<Unit>
}
