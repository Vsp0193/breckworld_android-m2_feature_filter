package com.breckworld.model.sendfeedback

data class FeedBackModel(
    val message: String,
    val status: String,
    val error: String,
    val error_description: String
)