package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.OfferDB
import com.breckworld.repository.remote.http.model.Offer

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 14:08
 *         E-mail: bondes87@gmail.com
 */
class OffersMapper : BaseMapper<List<Offer>, List<OfferDB>> {
    override fun map(entity: List<Offer>): List<OfferDB> {
        val newList: ArrayList<OfferDB> = ArrayList()
        entity.forEach { newList.add(OfferMapper().map(it)) }
        return newList
    }
}