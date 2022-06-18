package com.breckworld.repository.database.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.breckworld.repository.local.model.MarkerModel
import java.io.Serializable

@Entity
data class LocationStarDB(
    var campaignId: String,
    var description: String,
    var fullAddress: String,
    @PrimaryKey
    var id: Long,
    var image: String,
    var lat: Double,
    var lng: Double,
    var title: String,
    var videoId: String,
    var website: String
) : Serializable, MarkerModel {
    @Ignore
    override var distanceToMarker: Double = 0.0
    @Ignore
    override var bearingToMarker: Float = 0f
    @Ignore
    override var activated: Boolean = false
}