package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.SectorDB
import com.breckworld.repository.remote.http.model.Sector

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 14:08
 *         E-mail: bondes87@gmail.com
 */
class SectorMapper : BaseMapper<Sector, SectorDB> {
    override fun map(entity: Sector): SectorDB {
        return SectorDB(
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