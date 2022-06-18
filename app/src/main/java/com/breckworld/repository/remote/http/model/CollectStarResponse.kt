package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName

data class CollectStarResponse (
    @SerializedName("success")
    val success: Int = 0,
    @SerializedName("insertID")
    val insertId: String? = null,
    @SerializedName("error")
    val error: Int = 0,
    @SerializedName("error_message")
    val errorMessage: String? = null
)