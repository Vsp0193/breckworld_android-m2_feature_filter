package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.SectorDB
import com.breckworld.repository.remote.http.model.Sector

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 14:08
 *         E-mail: bondes87@gmail.com
 */
class SectorsMapper : BaseMapper<List<Sector>, List<SectorDB>> {
    override fun map(entity: List<Sector>): List<SectorDB> {
        val newList: ArrayList<SectorDB> = ArrayList()
        entity.forEach { newList.add(SectorMapper().map(it)) }
        return newList
    }
}