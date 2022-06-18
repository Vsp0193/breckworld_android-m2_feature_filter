package com.breckworld.model.signup

data class SignupModel(
    val code: Int,
    val message: String,
    val status: String,
    val user: String,
    val error: String,
    val error_description: String
)