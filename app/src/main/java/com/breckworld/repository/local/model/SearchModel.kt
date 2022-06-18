package com.breckworld.repository.local.model

import java.io.Serializable

/**
 * @author Dmytro Bondarenko
 *         Date: 05.06.2019
 *         Time: 10:39
 *         E-mail: bondes87@gmail.com
 */
data class SearchModel(
    val lat: Double?,
    val lng: Double?,
    val term: String?,
    val city: String?,
    val page: Int
) : Serializable