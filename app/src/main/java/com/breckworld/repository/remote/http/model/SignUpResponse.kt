package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Dmytro Bondarenko
 *         Date: 27.05.2019
 *         Time: 11:41
 *         E-mail: bondes87@gmail.com
 */
data class SignUpResponse(
    @SerializedName("user_created")
    var userCreated: String?,
    @SerializedName("error")
    override var error: String? = null,
    @SerializedName("error_description")
    override var errorDescription: String? = null,
    @SerializedName("error_message")
    override var errorMessage: String? = null
) : BaseResponse(), Serializable