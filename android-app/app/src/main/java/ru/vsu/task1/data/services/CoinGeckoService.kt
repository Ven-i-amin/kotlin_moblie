package ru.vsu.task1.data.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.data.models.coin.CoinName

interface CoinGeckoService {
    @GET("/api/v3/coins/list")
    suspend fun getCoins(): Response<List<CoinName>>

    @GET("/api/v3/coins/markets")
    suspend fun getMarketInfo(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("name") name: String = ""
    ): Response<List<CoinInfo>>
}