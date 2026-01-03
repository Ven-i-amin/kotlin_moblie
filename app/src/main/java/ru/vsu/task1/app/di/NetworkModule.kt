package ru.vsu.task1.app.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import ru.vsu.task1.data.services.BitgetService
import ru.vsu.task1.data.services.CoinGeckoService
import kotlin.jvm.java

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}


fun provideConverterFactory(): Converter.Factory =
    json.asConverterFactory("application/json".toMediaType())


fun provideRetrofit(
    jsonFactory: Converter.Factory,
    url: String
): Retrofit = Retrofit.Builder()
    .baseUrl(url)
    .addConverterFactory(jsonFactory)
    .build()

fun <T> provideService(retrofit: Retrofit, classType: Class<T>): T =
    retrofit.create(classType)

val networkModule = module {
    single { provideConverterFactory() }

    single(named("CoinGeckoRetrofit")) {
        provideRetrofit(get(), "https://api.coingecko.com/")
    }

    single(named("BitgetRetrofit")) {
        provideRetrofit(get(), "https://api.bitget.com/")
    }

    single {
        provideService(
            get(named("CoinGeckoRetrofit")),
            CoinGeckoService::class.java
        )
    }

    single {
        provideService(
            get(named("BitgetRetrofit")),
            BitgetService::class.java
        )
    }
}