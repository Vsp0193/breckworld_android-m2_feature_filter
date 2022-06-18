package com.breckworld.repository.database.mapper

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 14:05
 *         E-mail: bondes87@gmail.com
 */
interface BaseMapper<T, V> {
    fun map(entity: T): V
}