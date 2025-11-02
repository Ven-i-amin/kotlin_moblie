package ru.vsu.task1.api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinGekkoCoinInfoResponse(
    @SerialName("id") val id: String,
    @SerialName("symbol") val symbol: String,
    @SerialName("name") val name: String,
    @SerialName("web_slug") val webSlug: String,
    @SerialName("asset_platform_id") val assetPlatformId: String? = null,
    @SerialName("platforms") val platforms: Map<String, String>? = null,
    @SerialName("detail_platforms") val detailPlatforms: Map<String, String>? = null,
    @SerialName("block_time_in_minutes") val blockTimeInMinutes: Int? = null,
    @SerialName("hashing_algorithm") val hashingAlgorithm: String? = null,
    @SerialName("categories") val categories: List<String>? = null,
    @SerialName("preview_listing") val previewListing: Boolean = false,
    @SerialName("public_notice") val publicNotice: String? = null,
    @SerialName("additional_notices") val additionalNotices: List<String>? = null,
    @SerialName("localization") val localization: Map<String, String>? = null,
    @SerialName("description") val description: Map<String, String>? = null,
    @SerialName("links") val links: Links? = null,
    @SerialName("image") val image: CoinImage? = null,
    @SerialName("country_origin") val countryOrigin: String? = null,
    @SerialName("genesis_date") val genesisDate: String? = null,
    @SerialName("sentiment_votes_up_percentage") val sentimentVotesUpPercentage: Double? = null,
    @SerialName("sentiment_votes_down_percentage") val sentimentVotesDownPercentage: Double? = null,
    @SerialName("watchlist_portfolio_users") val watchlistPortfolioUsers: Int? = null,
    @SerialName("market_cap_rank") val marketCapRank: Int? = null,
    @SerialName("market_data") val marketData: MarketData? = null,
    @SerialName("status_updates") val statusUpdates: List<String>? = null,
    @SerialName("last_updated") val lastUpdated: String? = null
)

@Serializable
data class Links(
    @SerialName("homepage") val homepage: List<String>? = null,
    @SerialName("whitepaper") val whitePaper: String? = null,
    @SerialName("blockchain_site") val blockchainSite: List<String>? = null,
    @SerialName("official_forum_url") val officialForumUrl: List<String>? = null,
    @SerialName("chat_url") val chatUrl: List<String>? = null,
    @SerialName("announcement_url") val announcementUrl: List<String>? = null,
    @SerialName("twitter_screen_name") val twitterScreenName: String? = null,
    @SerialName("facebook_username") val facebookUsername: String? = null,
    @SerialName("subreddit_url") val subredditUrl: String? = null,
    @SerialName("repos_url") val reposUrl: Map<String, List<String>>? = null
)

@Serializable
data class CoinImage(
    @SerialName("thumb") val thumb: String? = null,
    @SerialName("small") val small: String? = null,
    @SerialName("large") val large: String? = null
)

@Serializable
data class MarketData(
    @SerialName("current_price") val currentPrice: Map<String, Double>? = null,
    @SerialName("total_value_locked") val totalValueLocked: Double? = null,
    @SerialName("mcap_to_tvl_ratio") val mcapToTvlRatio: Double? = null,
    @SerialName("fdv_to_tvl_ratio") val fdvToTvlRatio: Double? = null,
    @SerialName("roi") val roi: Double? = null,
    @SerialName("ath") val ath: Map<String, Double>? = null,
    @SerialName("ath_change_percentage") val athChangePercentage: Map<String, Double>? = null,
    @SerialName("ath_date") val athDate: Map<String, String>? = null,
    @SerialName("atl") val atl: Map<String, Double>? = null,
    @SerialName("atl_change_percentage") val atlChangePercentage: Map<String, Double>? = null,
    @SerialName("atl_date") val atlDate: Map<String, String>? = null,
    @SerialName("market_cap") val marketCap: Map<String, Double>? = null,
    @SerialName("market_cap_rank") val marketCapRank: Int? = null,
    @SerialName("fully_diluted_valuation") val fullyDilutedValuation: Map<String, Double>? = null,
    @SerialName("market_cap_fdv_ratio") val marketCapFdvRatio: Double? = null,
    @SerialName("total_volume") val totalVolume: Map<String, Double>? = null,
    @SerialName("high_24h") val high24h: Map<String, Double>? = null,
    @SerialName("low_24h") val low24h: Map<String, Double>? = null,
    @SerialName("price_change_24h") val priceChange24h: Double? = null,
    @SerialName("price_change_percentage_24h") val priceChangePercentage24h: Double? = null,
    @SerialName("price_change_percentage_7d") val priceChangePercentage7d: Double? = null,
    @SerialName("price_change_percentage_14d") val priceChangePercentage14d: Double? = null,
    @SerialName("price_change_percentage_30d") val priceChangePercentage30d: Double? = null,
    @SerialName("price_change_percentage_60d") val priceChangePercentage60d: Double? = null,
    @SerialName("price_change_percentage_200d") val priceChangePercentage200d: Double? = null,
    @SerialName("price_change_percentage_1y") val priceChangePercentage1y: Double? = null,
    @SerialName("market_cap_change_24h") val marketCapChange24h: Double? = null,
    @SerialName("market_cap_change_percentage_24h") val marketCapChangePercentage24h: Double? = null,
    @SerialName("total_supply") val totalSupply: Double? = null,
    @SerialName("max_supply") val maxSupply: Double? = null,
    @SerialName("max_supply_infinite") val maxSupplyInfinite: Boolean? = null,
    @SerialName("circulating_supply") val circulatingSupply: Double? = null,
    @SerialName("last_updated") val lastUpdated: String? = null
)
