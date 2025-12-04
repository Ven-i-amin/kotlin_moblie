package ru.vsu.task1.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.vsu.task1.api.domain.UserUseCase

val domainModule = module {
    singleOf(::UserUseCase)
}