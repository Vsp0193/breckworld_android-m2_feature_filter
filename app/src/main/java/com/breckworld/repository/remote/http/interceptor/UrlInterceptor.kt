package com.breckworld.repository.remote.http.interceptor

import com.breckworld.repository.Repository
import okhttp3.Interceptor
import okhttp3.Response

class UrlInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val newRequest = chain?.request()
            ?.newBuilder()
            ?.url(
                chain.request().url().newBuilder()
                    .addQueryParameter(ACCESS_TOKEN, Repository.getAccessToken())
                    .build()
            )
            ?.build()
        return chain?.proceed(newRequest!!)!!
    }

    companion object {
        private const val ACCESS_TOKEN = "access_token"
    }
}