package com.breckworld.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class StarDB(
    var dateAdded: String,
    var description: String,
    @PrimaryKey
    var entryId: Long,
    var image: String,
    var location: String,
    var pickLat: Double,
    var pickLng: Double,
    var redeemed: Int,
    var title: String,
    var tokenId: String,
    var tokenType: String,
    var videoId: String,
    var website: String
) : Serializable