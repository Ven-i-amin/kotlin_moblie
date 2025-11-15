package ru.vsu.task1.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.vsu.task1.COIN_GEKKO_API_KEY

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request
            .url
            .newBuilder()
            .addQueryParameter(
                name = "x_cg_demo_api_key",
                value = COIN_GEKKO_API_KEY
            ).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}