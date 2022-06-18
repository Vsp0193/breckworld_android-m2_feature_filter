package com.breckworld.app.repository

sealed class ResponseListener<T>(val data: T? = null, val failureData: String? = null, val errorMessage: String? = null) {

    class Loading<T> : ResponseListener<T>()

    class Success<T>(data: T? = null) : ResponseListener<T>(data = data)

    class Failure<T>(data: String = "") : ResponseListener<T>(failureData = data)

    class Error<String>(errorMessage: kotlin.String = "") : ResponseListener<String>(errorMessage = errorMessage.toString())

}