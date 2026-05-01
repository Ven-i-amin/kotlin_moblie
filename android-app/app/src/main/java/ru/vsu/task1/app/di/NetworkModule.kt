package ru.vsu.task1.app.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import ru.vsu.task1.BuildConfig
import ru.vsu.task1.data.services.BitgetService
import ru.vsu.task1.data.services.CoinGeckoService
import ru.vsu.task1.data.services.GatewayService
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

private const val CONNECT_TIMEOUT_SECONDS = 10L
private const val READ_TIMEOUT_SECONDS = 15L
private const val WRITE_TIMEOUT_SECONDS = 15L
private const val CALL_TIMEOUT_SECONDS = 20L

fun provideConverterFactory(): Converter.Factory =
    json.asConverterFactory("application/json".toMediaType())

fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
    .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
    .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
    .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
    .build()

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    jsonFactory: Converter.Factory,
    url: String
): Retrofit = Retrofit.Builder()
    .baseUrl(url)
    .client(okHttpClient)
    .addConverterFactory(jsonFactory)
    .build()

fun <T> provideService(retrofit: Retrofit, classType: Class<T>): T =
    retrofit.create(classType)

val networkModule = module {
    single { provideConverterFactory() }
    single { provideOkHttpClient() }

    single(named("CoinGeckoRetrofit")) {
        provideRetrofit(get(), get(), "https://api.coingecko.com/")
    }

    single(named("BitgetRetrofit")) {
        provideRetrofit(get(), get(), "https://api.bitget.com/")
    }

    single(named("GatewayRetrofit")) {
        provideRetrofit(get(), get(), BuildConfig.GATEWAY_BASE_URL)
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

    single {
        provideService(
            get(named("GatewayRetrofit")),
            GatewayService::class.java
        )
    }
}
