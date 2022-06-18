package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.StarDB
import com.breckworld.repository.remote.http.model.Star

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 14:08
 *         E-mail: bondes87@gmail.com
 */
class StarsMapper : BaseMapper<List<Star>, List<StarDB>> {
    override fun map(entity: List<Star>): List<StarDB> {
        val newList: ArrayList<StarDB> = ArrayList()
        entity.forEach { newList.add(StarMapper().map(it)) }
        return newList
    }
}