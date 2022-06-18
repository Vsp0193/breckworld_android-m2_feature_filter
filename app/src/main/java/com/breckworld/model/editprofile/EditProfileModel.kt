package com.breckworld.model.editprofile

data class EditProfileModel(
    val success: Int,
    val user_updated: String,
    val error: String,
    val error_description: String
)