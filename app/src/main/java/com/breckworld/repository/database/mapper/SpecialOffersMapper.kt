package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.SpecialOfferDB
import com.breckworld.repository.remote.http.model.Offer

class SpecialOffersMapper : BaseMapper<List<Offer>, List<SpecialOfferDB>> {
    override fun map(entity: List<Offer>): List<SpecialOfferDB> {
        val newList: ArrayList<SpecialOfferDB> = ArrayList()
        entity.forEach { newList.add(SpecialOfferMapper().map(it)) }
        return newList
    }
}