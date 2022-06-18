package com.breckworld.repository.database.mapper

import com.breckworld.repository.database.model.ProfileDB
import com.breckworld.repository.remote.http.model.Profile

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 14:08
 *         E-mail: bondes87@gmail.com
 */
class ProfileMapper : BaseMapper<Profile, ProfileDB> {
    override fun map(entity: Profile): ProfileDB {
        return ProfileDB(
            entity.email ?: "",
            entity.firstName ?: "",
            entity.lastName ?: "",
            entity.profilePic ?: "",
            entity.role ?: "",
            entity.specials ?: "",
            entity.userId ?: 0,
            entity.userLogin ?: ""
        )
    }
}