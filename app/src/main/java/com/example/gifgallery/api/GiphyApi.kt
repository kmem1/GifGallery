package com.example.gifgallery.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GiphyApi {

    private const val BASE_URL = "https://api.giphy.com"
    private const val API_KEY = "td61T4JMHqJU9YnLG8rNaIPS569r1Sts"

    const val PAGE_SIZE = 20
    const val MAX_PAGE_SIZE = 50
    const val INITIAL_LOAD_SIZE = PAGE_SIZE * 3

    private val clientBuilder by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()

                val newUrl = originalRequest.url().newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()

                val newRequest = originalRequest.newBuilder()
                    .url(newUrl)
                    .build()

                chain.proceed(newRequest)
            }
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()
    }

    val service: GiphyService by lazy { retrofit.create(GiphyService::class.java) }
}