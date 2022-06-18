package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.StarDB
import com.breckworld.repository.remote.http.model.Star

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 14:08
 *         E-mail: bondes87@gmail.com
 */
class StarMapper : BaseMapper<Star, StarDB> {
    override fun map(entity: Star): StarDB {
        return StarDB(
            entity.dateAdded ?: "",
            entity.description ?: "",
            entity.entryId ?: 0,
            entity.image ?: "",
            entity.location ?: "",
            entity.pickLat ?: 0.0,
            entity.pickLng ?: 0.0,
            entity.redeemed ?: 0,
            entity.title ?: "",
            entity.tokenId ?: "",
            entity.tokenType ?: "",
            entity.videoId ?: "",
            entity.website ?: ""
        )
    }
}