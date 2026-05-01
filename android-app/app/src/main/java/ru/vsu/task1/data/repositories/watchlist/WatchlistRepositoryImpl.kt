package ru.vsu.task1.data.repositories.watchlist

import ru.vsu.task1.data.models.backend.GatewayWatchlistRequest
import ru.vsu.task1.data.services.GatewayService

class WatchlistRepositoryImpl(
    private val gatewayService: GatewayService
) : WatchlistRepository {

    override suspend fun getWatchlist(authToken: String): List<String> {
        return gatewayService.getWatchlist("Bearer $authToken")
    }

    override suspend fun addCoinToWatchlist(authToken: String, coinId: String) {
        gatewayService.addToWatchlist(
            "Bearer $authToken",
            GatewayWatchlistRequest(coinId = coinId)
        )
    }

    override suspend fun removeCoinFromWatchlist(authToken: String, coinId: String) {
        gatewayService.removeFromWatchlist("Bearer $authToken", coinId)
    }
}
