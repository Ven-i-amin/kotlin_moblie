package ru.vsu.task1.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.vsu.task1.api.repositories.generic.CoinRepository
import ru.vsu.task1.api.repositories.generic.CoinRepositoryImpl
import ru.vsu.task1.api.repositories.home.HomeRepository
import ru.vsu.task1.api.repositories.home.HomeRepositoryImpl
import ru.vsu.task1.api.repositories.login.LoginRepository
import ru.vsu.task1.api.repositories.login.LoginRepositoryImpl
import ru.vsu.task1.api.repositories.trade.TradeRepository
import ru.vsu.task1.api.repositories.trade.TradeRepositoryImpl

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
    singleOf(::LoginRepositoryImpl) {
        bind<LoginRepository>()
    }
}