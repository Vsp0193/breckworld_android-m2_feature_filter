package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProfileResponse(
    @SerializedName("home_video_id")
    var homeVideoId: String?,
    var profile: Profile?,
    @SerializedName("search_categories")
    var searchCategories: List<String?>?,
    var wallets: List<Wallet>?,
    @SerializedName("error")
    override var error: String? = null,
    @SerializedName("error_description")
    override var errorDescription: String? = null,
    @SerializedName("error_message")
    override var errorMessage: String? = null
) : BaseResponse(), Serializable