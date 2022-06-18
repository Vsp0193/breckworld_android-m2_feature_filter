package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.LocationStarDB
import com.breckworld.repository.remote.http.model.LocationStar

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 14:08
 *         E-mail: bondes87@gmail.com
 */
class LocationStarMapper : BaseMapper<LocationStar, LocationStarDB> {
    override fun map(entity: LocationStar): LocationStarDB {
        return LocationStarDB(
            entity.campaignId ?: "",
            entity.description ?: "",
            entity.fullAddress ?: "",
            entity.id ?: 0,
            entity.image ?: "",
            entity.lat ?: 0.0,
            entity.lng ?: 0.0,
            entity.title ?: "",
            entity.videoId ?: "",
            entity.website ?: ""
        )
    }
}