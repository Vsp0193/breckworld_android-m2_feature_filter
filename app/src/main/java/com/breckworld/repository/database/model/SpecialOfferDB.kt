package com.breckworld.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class SpecialOfferDB(
    var description: String,
    var discountCode: String,
    var discountTitle: String,
    var discountValue: String,
    var fullAddress: String,
    @PrimaryKey
    var id: Long,
    var image: String,
    var lat: Double,
    var lng: Double,
    var category: String,
    var objectId: String,
    var title: String,
    var videoId: String,
    var dateAdded: String,
    var entryId: Long,
    var location: String,
    var pickLat: Double,
    var pickLng: Double,
    var redeemed: Int,
    var tokenId: String,
    var tokenType: String,
    var website: String
) : Serializable
