package ru.vsu.task1.data.repositories.watchlist

class WatchlistRepositoryImpl : WatchlistRepository {
    val watchlist = mutableListOf("bitcoin", "ethereum", "solana")

    override suspend fun getWatchlist(authToken: String): List<String> {
        return watchlist
    }

    override suspend fun addCoinToWatchlist(authToken: String, coinId: String) {
        watchlist.add(coinId)
    }

    override suspend fun removeCoinFromWatchlist(authToken: String, coinId: String) {
        watchlist.remove(coinId)
    }
}