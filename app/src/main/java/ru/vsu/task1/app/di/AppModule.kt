package ru.vsu.task1.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.vsu.task1.ui.screens.auth.AuthViewModel
import ru.vsu.task1.ui.screens.home.HomeViewModel
import ru.vsu.task1.ui.screens.trade.TradeViewModel

val appModule = module {
    viewModelOf(::TradeViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AuthViewModel)
}