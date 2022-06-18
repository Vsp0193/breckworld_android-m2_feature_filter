package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Dmytro Bondarenko
 *         Date: 13.06.2019
 *         Time: 15:03
 *         E-mail: bondes87@gmail.com
 */
class RedeemRequest(
    var lat: Double?,
    var lng: Double?,
    var adults: Int?,
    var children: Int?
) : Serializable