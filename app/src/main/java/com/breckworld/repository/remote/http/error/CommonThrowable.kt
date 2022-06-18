package com.breckworld.repository.remote.http.error

class CommonThrowable(message: String,
                      val errorsMap: Map<String, String>? = null,
                      val code: Int = 0) : Throwable(message)