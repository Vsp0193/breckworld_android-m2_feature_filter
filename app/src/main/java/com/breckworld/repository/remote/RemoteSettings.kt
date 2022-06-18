package com.breckworld.repository.remote

import com.breckworld.BuildConfig

object RemoteSettings {

    const val BASE_HTTP_URL = "https://viewing.online" //BuildConfig.BASE_URL

    const val READ_TIMEOUT = 30L
    const val CONNECT_TIMEOUT = 60L
    const val WRITE_TIMEOUT = 120L

}