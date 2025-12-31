package com.yesdan.dolarczlamonitor.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BinanceApiClient {
    private const val BASE_URL = "https://p2p.binance.com/"

    private class BinanceHeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestWithHeaders = originalRequest.newBuilder()
                .header("User-Agent", "Mozilla/5.0")
                .header("Content-Type", "application/json")
                .build()
            return chain.proceed(requestWithHeaders)
        }
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(BinanceHeaderInterceptor())
        .build()

    val api: BinanceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BinanceApi::class.java)
    }
}
