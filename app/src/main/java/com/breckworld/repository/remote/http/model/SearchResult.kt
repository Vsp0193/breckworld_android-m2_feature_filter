package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchResult(
    @SerializedName("badge_number")
    var badgeNumber: String?,
    var category: String?,
    var city: String?,
    var description: String?,
    var email: String?,
    @SerializedName("full_address")
    var fullAddress: String?,
    var id: Int?,
    var image: String?,
    var lat: Double?,
    var lng: Double?,
    var offers: List<Offer>?,
    var phone: String?,
    var title: String?,
    @SerializedName("video_id")
    var videoId: String?,
    @SerializedName("video_url")
    var videoUrl: String?,
    var website: String?
) : Serializable