package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AllObjectsResponse(
    @SerializedName("location_objects")
    var locationObjects: List<LocationObject>?,
    @SerializedName("location_stars")
    var locationStars: List<LocationStar>?,
    @SerializedName("error")
    override var error: String? = null,
    @SerializedName("error_description")
    override var errorDescription: String? = null,
    @SerializedName("error_message")
    override var errorMessage: String? = null
) : BaseResponse(), Serializable