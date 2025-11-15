package ru.vsu.task1.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.vsu.task1.api.viewmodels.trade.TradeScreenViewModel

val appModule = module {
    viewModelOf(::TradeScreenViewModel)
}