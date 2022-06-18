package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Profile(
    var email: String?,
    @SerializedName("first_name")
    var firstName: String?,
    @SerializedName("last_name")
    var lastName: String?,
    @SerializedName("profile_pic")
    var profilePic: String?,
    var role: String?,
    var specials: String?,
    @SerializedName("user_id")
    var userId: Int?,
    @SerializedName("user_login")
    var userLogin: String?
): Serializable