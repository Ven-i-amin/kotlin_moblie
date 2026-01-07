package ru.vsu.task1.app.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.vsu.task1.data.repositories.coininfo.CoinInfoRepository
import ru.vsu.task1.data.repositories.coininfo.CoinInfoRepositoryImpl
import ru.vsu.task1.data.repositories.transaction.TransactionRepository
import ru.vsu.task1.data.repositories.transaction.TransactionRepositoryImpl
import ru.vsu.task1.data.repositories.user.UserRepository
import ru.vsu.task1.data.repositories.user.UserRepositoryImpl
import ru.vsu.task1.data.repositories.coinhistory.CoinHistoryRepository
import ru.vsu.task1.data.repositories.coinhistory.CoinHistoryRepositoryImpl
import ru.vsu.task1.data.repositories.order.OrderRepository
import ru.vsu.task1.data.repositories.order.OrderRepositoryImpl
import ru.vsu.task1.data.repositories.usercoin.UserCoinRepository
import ru.vsu.task1.data.repositories.usercoin.UserCoinRepositoryImpl
import ru.vsu.task1.data.repositories.watchlist.WatchlistRepository
import ru.vsu.task1.data.repositories.watchlist.WatchlistRepositoryImpl

val dataModule = module {
    singleOf(::CoinHistoryRepositoryImpl) {
        bind<CoinHistoryRepository>()
    }
    singleOf(::TransactionRepositoryImpl) {
        bind<TransactionRepository>()
    }
    singleOf(::CoinInfoRepositoryImpl) {
        bind<CoinInfoRepository>()
    }
    singleOf(::UserRepositoryImpl) {
        bind<UserRepository>()
    }
    singleOf(::UserCoinRepositoryImpl) {
        bind<UserCoinRepository>()
    }
    singleOf(::OrderRepositoryImpl) {
        bind<OrderRepository>()
    }
    singleOf(::WatchlistRepositoryImpl) {
        bind<WatchlistRepository>()
    }
}