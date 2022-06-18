package com.breckworld.model.userprofile

data class Profile(
    val email: String,
    var first_name: String,
    var last_name: String,
    var profile_pic: String,
    val role: String,
    val user_id: Int
)