package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Offer(
    var description: String?,
    @SerializedName("discount_code")
    var discountCode: String?,
    @SerializedName("discount_title")
    var discountTitle: String?,
    @SerializedName("discount_value")
    var discountValue: String?,
    @SerializedName("full_address")
    var fullAddress: String?,
    var id: Long?,
    var image: String?,
    var lat: Double?,
    var lng: Double?,
    var category: String?,
    @SerializedName("object_id")
    var objectId: String?,
    var title: String?,
    @SerializedName("video_id")
    var videoId: String?,
    @SerializedName("date_added")
    var dateAdded: String?,
    @SerializedName("entry_id")
    var entryId: Long?,
    var location: String?,
    @SerializedName("pick_lat")
    var pickLat: Double?,
    @SerializedName("pick_lng")
    var pickLng: Double?,
    var redeemed: Int?,
    @SerializedName("token_id")
    var tokenId: String?,
    @SerializedName("token_type")
    var tokenType: String?,
    var website: String?
): Serializable