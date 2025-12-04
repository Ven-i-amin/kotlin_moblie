package ru.vsu.task1.data.repository.coin

import android.util.Log
import ru.vsu.task1.data.services.CoinService
import ru.vsu.task1.domain.models.coin.CoinInfo

class CoinRepositoryImpl(private val service: CoinService) : CoinRepository {
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