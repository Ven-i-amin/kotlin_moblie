package ru.vsu.task1.data.services

import androidx.compose.ui.text.TextGranularity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.vsu.task1.domain.models.coin.CoinMarketInfo
import ru.vsu.task1.domain.models.trade.CoinChart

interface BitgetService {
    @GET("/api/v2/spot/market/tickers")
    suspend fun getCoinMarketInfo(): Response<CoinMarketInfo>
    @GET("/api/v2/spot/market/tickers")
    suspend fun getCoinMarketInfo(
        @Query("symbol") symbol: String
    ): Response<CoinMarketInfo>

    @GET("/api/v2/spot/market/history-candles")
    suspend fun getMarketChartInfo(
        @Query("symbol") symbol: String,
        @Query("granularity") granularity: String,
        @Query("endTime") endTime: String
    ) : Response<CoinChart>

    @GET("/api/v2/spot/market/history-candles")
    suspend fun getMarketChartInfo(
        @Query("symbol") symbol: String,
        @Query("granularity") granularity: String,
        @Query("endTime") endTime: String,
        @Query("limit") limit: String
    ) : Response<CoinChart>

}