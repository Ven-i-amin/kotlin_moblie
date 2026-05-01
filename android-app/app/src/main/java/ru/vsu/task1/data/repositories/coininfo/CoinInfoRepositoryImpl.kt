package ru.vsu.task1.data.repositories.coininfo

import android.util.Log
import ru.vsu.task1.data.services.BitgetService
import ru.vsu.task1.data.services.CoinGeckoService
import ru.vsu.task1.data.models.coin.CoinInfo
import ru.vsu.task1.data.models.coin.CoinName

private const val TO_PERCENTAGES = 100

class CoinInfoRepositoryImpl(
    private val coinGeckoService: CoinGeckoService,
    private val bitgetService: BitgetService
) : CoinInfoRepository {
    override suspend fun getMarketCoinInfo(coinInfo: CoinInfo): CoinInfo {
        val response = bitgetService.getCoinMarketInfo(coinInfo.bitgetSymbol)

        if (response.isSuccessful && response.body()!!.data.isNotEmpty()) {
            val marketData = response.body()!!.data[0]

            return coinInfo.apply {
                currentPrice = marketData.price
                priceChange24h = marketData.price - marketData.openPrice
                priceChangePercentage24h = marketData.priceChangeCoef24h * TO_PERCENTAGES
            }
        } else {
            Log.e("CoinFetchException", response.errorBody()?.string() ?: "not enabled")
            throw Exception(response.message())
        }
    }

    override suspend fun getMarketCoinList(coinInfoList: List<CoinInfo>): List<CoinInfo> {
        val response = bitgetService.getCoinMarketInfo()

        if (response.isSuccessful && response.body()!!.data.isNotEmpty()) {
            val marketData = response.body()!!.data

            return coinInfoList
                .map { coinInfo ->
                    val marketData = marketData.find { it.symbol == coinInfo.bitgetSymbol }

                    if (marketData == null) {
                        return@map null
                    }

                    coinInfo.apply {
                        currentPrice = marketData.price
                        priceChange24h = marketData.price - marketData.openPrice
                        priceChangePercentage24h = marketData.priceChangeCoef24h * TO_PERCENTAGES
                    }
                }
                .filterNotNull()
        } else {
            Log.e("CoinFetchException", response.errorBody()?.string() ?: "not enabled")
            throw Exception(response.message())
        }
    }

    override suspend fun getCoinList(): List<CoinInfo> {
        return getCoinList("usd")
    }

    override suspend fun getCoinList(vsCurrency: String): List<CoinInfo> {
        val response = coinGeckoService.getMarketInfo(vsCurrency)

        Log.d("CoinRepositoryImpl", "getCoinsInfo")

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            Log.e("CoinFetchException", response.errorBody()?.string() ?: "not enabled")
            throw Exception(response.message())
        }
    }

    override suspend fun getCoinNameList(): List<CoinName> {
        val response = coinGeckoService.getCoins()

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            Log.e("CoinFetchException", response.errorBody()?.string() ?: "not enabled")
            throw Exception(response.message())
        }
    }
}