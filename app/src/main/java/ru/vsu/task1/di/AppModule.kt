package ru.vsu.task1.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.vsu.task1.api.viewmodels.auth.AuthViewModel
import ru.vsu.task1.api.viewmodels.home.HomeViewModel
import ru.vsu.task1.api.viewmodels.trade.TradeViewModel

val appModule = module {
    viewModelOf(::TradeViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AuthViewModel)
}