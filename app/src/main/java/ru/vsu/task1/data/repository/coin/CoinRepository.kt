package ru.vsu.task1.data.repository.coin

import ru.vsu.task1.domain.models.coin.CoinInfo
import ru.vsu.task1.domain.models.coin.CoinName
import ru.vsu.task1.domain.models.home.Transaction

interface CoinRepository {
    suspend fun getMarketCoinInfo(coinInfo: CoinInfo): CoinInfo
    suspend fun getMarketCoinList(coinInfoList: List<CoinInfo>): List<CoinInfo>
    suspend fun getCoinList(): List<CoinInfo>
    suspend fun getCoinList(vsCurrency: String): List<CoinInfo>
    suspend fun getCoinNameList(): List<CoinName>
}