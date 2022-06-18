package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Dmytro Bondarenko
 *         Date: 27.05.2019
 *         Time: 11:41
 *         E-mail: bondes87@gmail.com
 */
data class LoginResponse(
    @SerializedName("access_token")
    var accessToken: String?,
    @SerializedName("expires_in")
    var expiresIn: Int?,
    @SerializedName("refresh_token")
    var refreshToken: String?,
    @SerializedName("scope")
    var scope: Any?,
    @SerializedName("token_type")
    var tokenType: String?,
    @SerializedName("error")
    override var error: String? = null,
    @SerializedName("error_description")
    override var errorDescription: String? = null,
    @SerializedName("error_message")
    override var errorMessage: String? = null
) : BaseResponse(), Serializable