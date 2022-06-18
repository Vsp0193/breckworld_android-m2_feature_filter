package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Star(
    @SerializedName("date_added")
    var dateAdded: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("entry_id")
    var entryId: Long?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("location")
    var location: String?,
    @SerializedName("pick_lat")
    var pickLat: Double?,
    @SerializedName("pick_lng")
    var pickLng: Double?,
    @SerializedName("redeemed")
    var redeemed: Int?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("token_id")
    var tokenId: String?,
    @SerializedName("token_type")
    var tokenType: String?,
    @SerializedName("video_id")
    var videoId: String?,
    @SerializedName("website")
    var website: String?
) : Serializable