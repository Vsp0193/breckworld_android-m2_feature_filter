package com.breckworld.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ProfileDB(
    var email: String,
    var firstName: String,
    var lastName: String,
    var profilePic: String,
    var role: String,
    var specials: String,
    @PrimaryKey
    var userId: Int,
    var userLogin: String
) : Serializable