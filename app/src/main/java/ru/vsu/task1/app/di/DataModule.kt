package ru.vsu.task1.app.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.vsu.task1.data.repository.coin.CoinRepository
import ru.vsu.task1.data.repository.coin.CoinRepositoryImpl
import ru.vsu.task1.data.repository.home.HomeRepository
import ru.vsu.task1.data.repository.home.HomeRepositoryImpl
import ru.vsu.task1.data.repository.auth.AuthRepository
import ru.vsu.task1.data.repository.auth.AuthRepositoryImpl
import ru.vsu.task1.data.repository.trade.TradeRepository
import ru.vsu.task1.data.repository.trade.TradeRepositoryImpl

val dataModule = module {
    singleOf(::TradeRepositoryImpl) {
        bind<TradeRepository>()
    }
    singleOf(::HomeRepositoryImpl) {
        bind<HomeRepository>()
    }
    singleOf(::CoinRepositoryImpl) {
        bind<CoinRepository>()
    }
    singleOf(::AuthRepositoryImpl) {
        bind<AuthRepository>()
    }
}