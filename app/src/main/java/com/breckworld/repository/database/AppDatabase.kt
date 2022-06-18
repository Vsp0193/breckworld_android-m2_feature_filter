package com.breckworld.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.breckworld.extensions.Constants
import com.breckworld.repository.database.model.*

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 13:01
 *         E-mail: bondes87@gmail.com
 */
@Database(
    entities = [
        TableCache::class,
        LocationStarDB::class,
        StarDB::class,
        ProfileDB::class,
        OfferDB::class,
        LocationOfferDB::class,
        SpecialOfferDB::class
    ], version = Constants.DATABASE_VERSION
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseRepository(): DatabaseController
}