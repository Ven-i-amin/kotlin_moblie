package ru.vsu.task1.data.repositories.watchlist

interface WatchlistRepository {
    suspend fun getWatchlist(authToken: String) : List<String>
    suspend fun addCoinToWatchlist(authToken: String, coinId: String)
    suspend fun removeCoinFromWatchlist(authToken: String, coinId: String)
}