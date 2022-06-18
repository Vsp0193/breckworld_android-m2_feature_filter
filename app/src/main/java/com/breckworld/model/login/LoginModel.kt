package com.breckworld.model.login

data class LoginModel(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val scope: Any,
    val token_type: String,
    val error: String,
    val error_description: String

)