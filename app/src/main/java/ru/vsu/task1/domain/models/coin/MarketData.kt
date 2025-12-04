package ru.vsu.task1.domain.models.coin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketData(
    @SerialName("current_price")
    val currentPrice: Map<String, Double>? = null,

    @SerialName("ath")
    val ath: Map<String, Double>? = null,

    @SerialName("ath_change_percentage")
    val athChangePercentage: Map<String, Double>? = null,

    @SerialName("ath_date")
    val athDate: Map<String, String>? = null,

    @SerialName("atl")
    val atl: Map<String, Double>? = null,

    @SerialName("atl_change_percentage")
    val atlChangePercentage: Map<String, Double>? = null,

    @SerialName("atl_date")
    val atlDate: Map<String, String>? = null,

    @SerialName("market_cap")
    val marketCap: Map<String, Double>? = null,

    @SerialName("fully_diluted_valuation")
    val fullyDilutedValuation: Map<String, Double>? = null,

    @SerialName("market_cap_fdv_ratio")
    val marketCapFdvRatio: Double? = null,

    @SerialName("total_volume")
    val totalVolume: Map<String, Double>? = null,

    @SerialName("high_24h")
    val high24h: Map<String, Double>? = null,

    @SerialName("low_24h")
    val low24h: Map<String, Double>? = null,

    @SerialName("price_change_24h")
    val priceChange24h: Double? = null,

    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: Double? = null,

    @SerialName("price_change_percentage_7d")
    val priceChangePercentage7d: Double? = null,

    @SerialName("price_change_percentage_14d")
    val priceChangePercentage14d: Double? = null,

    @SerialName("price_change_percentage_30d")
    val priceChangePercentage30d: Double? = null,

    @SerialName("price_change_percentage_60d")
    val priceChangePercentage60d: Double? = null,

    @SerialName("price_change_percentage_200d")
    val priceChangePercentage200d: Double? = null,

    @SerialName("price_change_percentage_1y")
    val priceChangePercentage1y: Double? = null,

    @SerialName("market_cap_change_24h")
    val marketCapChange24h: Double? = null,

    @SerialName("market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: Double? = null,

    @SerialName("price_change_24h_in_currency")
    val priceChange24hInCurrency: Map<String, Double>? = null,

    @SerialName("price_change_percentage_1h_in_currency")
    val priceChangePercentage1hInCurrency: Map<String, Double>? = null,

    @SerialName("price_change_percentage_24h_in_currency")
    val priceChangePercentage24hInCurrency: Map<String, Double>? = null,

    @SerialName("price_change_percentage_7d_in_currency")
    val priceChangePercentage7dInCurrency: Map<String, Double>? = null,

    @SerialName("price_change_percentage_14d_in_currency")
    val priceChangePercentage14dInCurrency: Map<String, Double>? = null,

    @SerialName("price_change_percentage_30d_in_currency")
    val priceChangePercentage30dInCurrency: Map<String, Double>? = null,

    @SerialName("price_change_percentage_60d_in_currency")
    val priceChangePercentage60dInCurrency: Map<String, Double>? = null,

    @SerialName("price_change_percentage_200d_in_currency")
    val priceChangePercentage200dInCurrency: Map<String, Double>? = null,

    @SerialName("price_change_percentage_1y_in_currency")
    val priceChangePercentage1yInCurrency: Map<String, Double>? = null,

    @SerialName("market_cap_change_24h_in_currency")
    val marketCapChange24hInCurrency: Map<String, Double>? = null,

    @SerialName("market_cap_change_percentage_24h_in_currency")
    val marketCapChangePercentage24hInCurrency: Map<String, Double>? = null,

    @SerialName("last_updated")
    val lastUpdated: String? = null
)