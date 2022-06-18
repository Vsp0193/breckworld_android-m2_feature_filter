package com.breckworld.repository.remote.http.api

import com.breckworld.repository.remote.RemoteSettings
import com.breckworld.repository.remote.RemoteSettings.CONNECT_TIMEOUT
import com.breckworld.repository.remote.RemoteSettings.READ_TIMEOUT
import com.breckworld.repository.remote.RemoteSettings.WRITE_TIMEOUT
import com.breckworld.repository.remote.http.interceptor.HeaderInterceptor
import com.breckworld.repository.remote.http.interceptor.UrlInterceptor
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitCreator {

    fun initApi(): Api {
        val retrofit = initRetrofit()
        return retrofit.create(Api::class.java)
    }

    private fun initRetrofit(): Retrofit {
        val logInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val headerInterceptor = HeaderInterceptor()

        val client = OkHttpClient.Builder().apply {
            addInterceptor(logInterceptor)
            addInterceptor(headerInterceptor)
            addInterceptor(UrlInterceptor())
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        }

        return Retrofit.Builder()
            .baseUrl(RemoteSettings.BASE_HTTP_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client.build())
            .build()
    }
}