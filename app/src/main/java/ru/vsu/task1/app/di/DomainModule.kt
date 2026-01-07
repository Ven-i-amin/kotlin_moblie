package ru.vsu.task1.app.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.vsu.task1.domain.usecases.CoinUseCase
import ru.vsu.task1.domain.usecases.ProfileUseCase
import ru.vsu.task1.domain.usecases.UserCoinUseCase

val domainModule = module {
    singleOf(::ProfileUseCase)
    singleOf(::CoinUseCase)
    singleOf(::UserCoinUseCase)
}