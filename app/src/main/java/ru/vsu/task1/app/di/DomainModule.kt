package ru.vsu.task1.app.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.vsu.task1.domain.usecases.UserUseCase

val domainModule = module {
    singleOf(::UserUseCase)
}