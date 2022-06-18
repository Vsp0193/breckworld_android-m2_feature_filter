package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Dmytro Bondarenko
 *         Date: 27.05.2019
 *         Time: 11:37
 *         E-mail: bondes87@gmail.com
 */
class LoginRequest(
    @SerializedName("client_id")
    var clientId: String = "ViewingAPI1.0/iOSApp",
    @SerializedName("client_secret")
    var clientSecret: String = "TESTINGSECRETKEY",
    @SerializedName("grant_type")
    var grantType: String = "password",
    @SerializedName("scope")
    var scope: String? = null,
    @SerializedName("username")
    var username: String?,
    @SerializedName("password")
    var password: String?
): Serializable