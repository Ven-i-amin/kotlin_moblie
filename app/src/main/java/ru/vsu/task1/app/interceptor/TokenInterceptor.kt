package ru.vsu.task1.app.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.vsu.task1.COIN_GECKO_API_KEY

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val host = request.url.host

        if (host != "api.coingecko.com") {
            return chain.proceed(request)
        }

        val url = request
            .url
            .newBuilder()
            .addQueryParameter(
                name = "x_cg_demo_api_key",
                value = COIN_GECKO_API_KEY
            ).build()
        val updatedRequest = request.newBuilder().url(url).build()
        return chain.proceed(updatedRequest)
    }
}
