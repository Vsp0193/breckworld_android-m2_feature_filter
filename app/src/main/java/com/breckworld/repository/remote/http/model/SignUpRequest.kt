package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Dmytro Bondarenko
 *         Date: 27.05.2019
 *         Time: 11:37
 *         E-mail: bondes87@gmail.com
 */
class SignUpRequest(
    @SerializedName("username")
    var username: String?,
    @SerializedName("password")
    var password: String?,
    @SerializedName("first_name")
    var firstName: String?,
    @SerializedName("last_name")
    var lastName: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("picture")
    var picture: String? = "https://viewing.online/wp-content/uploads/2018/05/avatar-business-man-graphic-vector-9646660.jpg"
): Serializable