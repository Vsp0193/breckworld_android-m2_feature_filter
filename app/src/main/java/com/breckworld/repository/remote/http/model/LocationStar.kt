package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocationStar(
    @SerializedName("campaign_id")
    var campaignId: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("full_address")
    var fullAddress: String?,
    @SerializedName("id")
    var id: Long?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("lat")
    var lat: Double?,
    @SerializedName("lng")
    var lng: Double?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("video_id")
    var videoId: String?,
    @SerializedName("website")
    var website: String?
) : Serializable