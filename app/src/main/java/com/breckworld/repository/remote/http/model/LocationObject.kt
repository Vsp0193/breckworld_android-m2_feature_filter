package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocationObject(
    var category: String?,
    var city: String?,
    var description: String?,
    var email: String?,
    @SerializedName("full_address")
    var fullAddress: String?,
    var id: String?,
    var image: String?,
    var lat: String?,
    var lng: String?,
    var offers: List<Offer?>?,
    var phone: String?,
    var title: String?,
    @SerializedName("video_id")
    var videoId: String?,
    @SerializedName("video_url")
    var videoUrl: String?,
    var website: String?
): Serializable