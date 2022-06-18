package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.LocationStarDB
import com.breckworld.repository.remote.http.model.LocationStar

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 14:08
 *         E-mail: bondes87@gmail.com
 */
class LocationStarsMapper : BaseMapper<List<LocationStar>, List<LocationStarDB>> {
    override fun map(entity: List<LocationStar>): List<LocationStarDB> {
        val newList: ArrayList<LocationStarDB> = ArrayList()
        entity.forEach { newList.add(LocationStarMapper().map(it)) }
        return newList
    }
}