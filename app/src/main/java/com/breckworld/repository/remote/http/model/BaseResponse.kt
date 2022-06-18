package com.breckworld.repository.remote.http.model

abstract class BaseResponse {
    abstract var error: String?
    abstract var errorDescription: String?
    abstract var errorMessage: String?
}