package ru.vsu.task1.data.repository.coin

import ru.vsu.task1.domain.models.coin.CoinInfo

interface CoinRepository {
    suspend fun getCoinInfo(id: String) : CoinInfo
}