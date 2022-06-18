package com.breckworld.repository.remote.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author Dmytro Bondarenko
 *         Date: 13.06.2019
 *         Time: 15:03
 *         E-mail: bondes87@gmail.com
 */
class UpdateProfileRequest(
    @SerializedName("first_name")
    var firstName: String? = null,
    @SerializedName("last_name")
    var lastName: String? = null,
    var password: String? = null,
    var email: String? = null,
    var picture: String? = null
) : Serializable