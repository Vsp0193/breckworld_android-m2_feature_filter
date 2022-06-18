package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName

data class RedeemResponse(
    val success: Int,
    @SerializedName("error")
    override var error: String? = null,
    @SerializedName("error_description")
    override var errorDescription: String? = null,
    @SerializedName("error_message")
    override var errorMessage: String? = null
) : BaseResponse()