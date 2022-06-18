package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Sector(
    @SerializedName("date_added")
    var dateAdded: String?,
    var description: String?,
    @SerializedName("entry_id")
    var entryId: Int?,
    var image: String?,
    var location: String?,
    @SerializedName("pick_lat")
    var pickLat: Double?,
    @SerializedName("pick_lng")
    var pickLng: Double?,
    var redeemed: Int?,
    var title: String?,
    @SerializedName("token_id")
    var tokenId: String?,
    @SerializedName("token_type")
    var tokenType: String?,
    @SerializedName("video_id")
    var videoId: String?,
    var website: String?
): Serializable