package ru.vsu.task1.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.vsu.task1.ui.navigation.AppBarViewModel
import ru.vsu.task1.ui.screens.auth.AuthViewModel
import ru.vsu.task1.ui.screens.coins.CoinsViewModel
import ru.vsu.task1.ui.screens.home.HomeViewModel
import ru.vsu.task1.ui.screens.portfolio.PortfolioViewModel
import ru.vsu.task1.ui.screens.trade.TradeViewModel

val appModule = module {
    single { AppBarViewModel() }
    viewModelOf(::TradeViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::CoinsViewModel)
    viewModelOf(::PortfolioViewModel)
}

