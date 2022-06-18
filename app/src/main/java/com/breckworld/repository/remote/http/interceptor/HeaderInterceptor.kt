package com.breckworld.repository.remote.http.interceptor

import android.util.Log
import com.breckworld.App
import com.breckworld.extensions.hasNetworkConnection
import com.breckworld.repository.Repository
import com.breckworld.repository.remote.http.error.NoNetworkException
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {

        //check internet
        if (!App.applicationContext().hasNetworkConnection())
            throw NoNetworkException()

        val newRequest = chain?.request()?.newBuilder()?.apply {
            header(CONTENT, CONTENT_TYPE)
            //header(ACCEPT, ACCEPT_TYPE)
            //header(AUTHORIZATION, Repository.getToken())
            header(LIVE_AUTHORIZATION, Repository.getToken())
        }?.build()
        Log.d(TAG, "$TAG_HEADER $AUTHORIZATION: " + newRequest?.header(AUTHORIZATION))
        Log.d(TAG, "$TAG_HEADER $ACCEPT: " + newRequest?.header(ACCEPT))
        return chain?.proceed(newRequest!!)!!
    }

    companion object {

        private const val TAG = "RETROFIT"
        private const val TAG_HEADER = "HEADER_INTERCEPTOR"

        private const val AUTHORIZATION = "Authorization"
        private const val LIVE_AUTHORIZATION = "LiveAuthorization"
        private const val AUTHORIZATION_TYPE = "Bearer"
        private const val ACCEPT = "Accept"
        private const val CONTENT = "Content-Type"
        private const val CONTENT_TYPE = "application/json"
        private const val ACCEPT_TYPE = "application/json"
    }
}