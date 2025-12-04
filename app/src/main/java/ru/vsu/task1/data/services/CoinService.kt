package ru.vsu.task1.data.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vsu.task1.domain.models.coin.CoinInfo
import ru.vsu.task1.domain.models.trade.MarketChart

interface CoinService {
    /**
     * @param id id криптовалюты (например, "bitcoin").
     * @param vsCurrency валюта для сравнения (например, "usd").
     * @param days количество дней для загрузки данных.
     */
    @GET("api/v3/coins/{id}/market_chart")
    suspend fun getPrices(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String,
        @Query("days") days: String,
    ): Response<MarketChart>

    @GET("api/v3/coins/{id}?tickers=false&community_data=false&developer_data=false&sparkline=false&market_data=true")
    suspend fun getCoinInfo(
        @Path("id") id: String
    ): Response<CoinInfo>
}