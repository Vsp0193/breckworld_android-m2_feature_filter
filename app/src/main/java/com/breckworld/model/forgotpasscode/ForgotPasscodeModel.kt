package com.breckworld.model.forgotpasscode

data class ForgotPasscodeModel(
    val message: String,
    val status: String,
    val error: String,
    val error_description: String
)