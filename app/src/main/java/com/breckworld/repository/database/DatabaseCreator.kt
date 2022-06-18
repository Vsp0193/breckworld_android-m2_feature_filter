package com.breckworld.repository.database

import androidx.room.Room
import com.breckworld.App

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 12:03
 *         E-mail: bondes87@gmail.com
 */
object DatabaseCreator {
    fun initDatabase(): AppDatabase {
        return Room.databaseBuilder(App.applicationContext(), AppDatabase::class.java, "breckworld")
            .fallbackToDestructiveMigration()
            .build()
    }
}