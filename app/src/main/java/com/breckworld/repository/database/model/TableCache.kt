package com.breckworld.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 12:06
 *         E-mail: bondes87@gmail.com
 */
@Entity
data class TableCache(
    @PrimaryKey
    var tableName: String,
    var lastUpdate: Long,
    var lat: Double?,
    var lng: Double?,
    var needUpdate: Boolean
)