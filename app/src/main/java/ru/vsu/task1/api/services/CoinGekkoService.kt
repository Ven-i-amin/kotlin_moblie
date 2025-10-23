package ru.vsu.task1.api.services

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

import ru.vsu.task1.api.responses.CoinGeckoPriceResponse

interface CoinGekkoService {
    /**
     * @param id id криптовалюты (например, "bitcoin").
     * @param vs_currency валюта для сравнения (например, "usd").
     * @param days количество дней для загрузки данных.
     * @param apiKey ваш API ключ.
     */
    @GET("/coins/{id}/market_chart")
    suspend fun getPrices(
        @Path("id") id: String,
        @Query("vs_currency") vs_currency: String,
        @Query("days") days: String,
        @Query("x_cg_demo_api_key") apiKey: String
    ): CoinGeckoPriceResponse
}