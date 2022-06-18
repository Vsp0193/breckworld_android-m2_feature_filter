package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.LocationOfferDB
import com.breckworld.repository.remote.http.model.LocationObject

class LocationOfferMapper : BaseMapper<LocationObject, List<LocationOfferDB>> {
    override fun map(entity: LocationObject): List<LocationOfferDB> =
        entity.offers?.map {
            LocationOfferDB(
                it?.description.orEmpty(),
                it?.discountCode.orEmpty(),
                it?.discountTitle.orEmpty(),
                it?.discountValue.orEmpty(),
                entity.fullAddress.orEmpty(),
                it?.id ?: 0,
                it?.image.orEmpty(),
                entity.lat?.toDouble() ?: 0.0,
                entity.lng?.toDouble() ?: 0.0,
                entity.category.orEmpty(),
                it?.objectId.orEmpty(),
                it?.title.orEmpty(),
                it?.videoId.orEmpty(),
                it?.dateAdded.orEmpty(),
                it?.entryId ?: 0,
                it?.lat ?: 0.0,
                it?.lng ?: 0.0,
                it?.website.orEmpty()
            )
        } ?: emptyList()
}