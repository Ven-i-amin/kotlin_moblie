package ru.vsu.task1.domain.models.coin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
