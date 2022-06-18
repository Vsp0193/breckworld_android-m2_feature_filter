package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    val success: Int,
    val user_updated: String,
    @SerializedName("error")
    override var error: String? = null,
    @SerializedName("error_description")
    override var errorDescription: String? = null,
    @SerializedName("error_message")
    override var errorMessage: String? = null
) : BaseResponse()