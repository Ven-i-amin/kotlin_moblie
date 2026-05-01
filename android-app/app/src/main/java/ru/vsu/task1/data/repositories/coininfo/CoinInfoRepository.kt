package ru.vsu.task1.data.repositories.coininfo

import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.data.models.coin.CoinName

interface CoinInfoRepository {
    suspend fun getMarketCoinInfo(coinInfo: CoinInfo): CoinInfo
    suspend fun getMarketCoinList(coinInfoList: List<CoinInfo>): List<CoinInfo>
    suspend fun getCoinList(): List<CoinInfo>
    suspend fun getCoinList(vsCurrency: String): List<CoinInfo>
    suspend fun getCoinNameList(): List<CoinName>
}