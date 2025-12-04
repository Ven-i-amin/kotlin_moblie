package ru.vsu.task1.api.repositories.generic

import android.util.Log
import ru.vsu.task1.api.models.trade.CoinInfo
import ru.vsu.task1.api.services.CoinGeckoService

class CoinRepositoryImpl(private val service: CoinGeckoService) : CoinRepository {
    override suspend fun getCoinInfo(id: String): CoinInfo {
        val response = service.getCoinInfo(
            id = id
        )

        Log.d("CoinInfo", "CoinGot")

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            Log.e("CoinError", response.errorBody()?.string() ?: "not enabled")
            throw Exception(response.message())
        }
    }
}