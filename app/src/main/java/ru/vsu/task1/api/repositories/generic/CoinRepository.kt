package ru.vsu.task1.api.repositories.generic

import ru.vsu.task1.api.models.trade.CoinInfo

interface CoinRepository {
    suspend fun getCoinInfo(id: String) : CoinInfo
}