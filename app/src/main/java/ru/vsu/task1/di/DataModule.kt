package ru.vsu.task1.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.vsu.task1.api.repositories.trade.TradeRepository
import ru.vsu.task1.api.repositories.trade.TradeRepositoryImpl

val dataModule = module {
    singleOf(::TradeRepositoryImpl) {
        bind<TradeRepository>()
    }
}