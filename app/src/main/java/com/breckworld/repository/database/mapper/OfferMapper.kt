package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.OfferDB
import com.breckworld.repository.remote.http.model.Offer

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 14:08
 *         E-mail: bondes87@gmail.com
 */
class OfferMapper : BaseMapper<Offer, OfferDB> {
    override fun map(entity: Offer): OfferDB {
        return OfferDB(
            entity.description ?: "",
            entity.discountCode ?: "",
            entity.discountTitle ?: "",
            entity.discountValue ?: "",
            entity.fullAddress ?: "",
            entity.id ?: 0L,
            entity.image ?: "",
            entity.lat ?: 0.0,
            entity.lng ?: 0.0,
            entity.category.orEmpty(),
            entity.objectId ?: "",
            entity.title ?: "",
            entity.videoId ?: "",
            entity.dateAdded ?: "",
            entity.entryId ?: 0L,
            entity.location ?: "",
            entity.pickLat ?: 0.0,
            entity.pickLng ?: 0.0,
            entity.redeemed ?: 0,
            entity.tokenId ?: "",
            entity.tokenType ?: "",
            entity.website ?: ""
        )
    }
}