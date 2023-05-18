package com.simplation.learnpaging.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    val BASE_URL = ""

    val API_KEY = ""

    var retrofitClient: RetrofitClient?= null
    lateinit var retrofit: Retrofit

    fun getInstance(): RetrofitClient {
        if (retrofitClient == null) {
            retrofitClient = RetrofitClient()
        }
        return retrofitClient as RetrofitClient
    }

    fun getRetrofitClient() {
        retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(Interceptor {
            val original = it.request()
            val url = original.url().newBuilder().addQueryParameter("apikey", API_KEY).build()
            val request = original.newBuilder().url(url).build()

            return@Interceptor it.proceed(request)
        })

        return httpClient.build()
    }

    fun getApi(): Api {
        return retrofit.create(Api::class.java)
    }

}