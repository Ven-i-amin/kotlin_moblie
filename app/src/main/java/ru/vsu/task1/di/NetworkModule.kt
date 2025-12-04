package ru.vsu.task1.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import ru.vsu.task1.api.services.CoinGeckoService
import kotlin.jvm.java

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}


fun provideConverterFactory(): Converter.Factory = json.asConverterFactory("application/json".toMediaType())


fun provideRetrofit(
    jsonFactory: Converter.Factory
): Retrofit = Retrofit.Builder()
    .baseUrl("https://api.coingecko.com/")
    .addConverterFactory(jsonFactory)
    .build()

fun provideService(retrofit: Retrofit): CoinGeckoService =
    retrofit.create(CoinGeckoService::class.java)

val networkModule = module {
    single { provideConverterFactory() }
    single { provideRetrofit(get()) }
    single { provideService(get()) }
}