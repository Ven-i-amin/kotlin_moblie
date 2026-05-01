package ru.vsu.task1.app.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.vsu.task1.data.usecases.CoinUseCase
import ru.vsu.task1.data.usecases.AuthUseCase
import ru.vsu.task1.data.usecases.TransactionUseCase
import ru.vsu.task1.data.usecases.UserCoinUseCase
import ru.vsu.task1.data.usecases.UserUseCase

val domainModule = module {
    singleOf(::AuthUseCase)
    singleOf(::CoinUseCase)
    singleOf(::UserCoinUseCase)
    singleOf(::UserUseCase)
    singleOf(::TransactionUseCase)
}