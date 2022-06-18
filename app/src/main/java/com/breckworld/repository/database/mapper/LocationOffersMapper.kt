package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.LocationOfferDB
import com.breckworld.repository.remote.http.model.LocationObject

class LocationOffersMapper : BaseMapper<List<LocationObject>, List<LocationOfferDB>> {
    override fun map(entity: List<LocationObject>): List<LocationOfferDB> {
        val newList: ArrayList<LocationOfferDB> = ArrayList()
        entity.forEach { newList.addAll(LocationOfferMapper().map(it)) }
        return newList
    }
}