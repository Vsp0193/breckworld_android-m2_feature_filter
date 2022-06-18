package com.breckworld.repository.database

import android.location.Location
import androidx.lifecycle.LiveData
import com.breckworld.repository.database.model.*

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 13:05
 *         E-mail: bondes87@gmail.com
 */
interface DatabaseRepository {

    // Table Cache
    fun insertTableCacheData(data: TableCache)

    fun getTableCacheData(tableName: String): List<TableCache>

    // Location Stars
    fun updateLocationStarsDB(locationStarsDB: List<LocationStarDB>, location: Location)
    fun insertLocationStarsDB(locationStarsDB: List<LocationStarDB>)
    fun getLocationStarsDBLiveData(): LiveData<List<LocationStarDB>>
    fun getLocationStarsWithoutCollectedLiveData(): LiveData<List<LocationStarDB>>

    // Location Offers
    fun insertLocationOffersDB(locationOffersDB: List<LocationOfferDB>)
    fun updateLocationOffersDB(locationOffersDB: List<LocationOfferDB>, location: Location)
    fun getLocationOffersDBLiveData(): LiveData<List<LocationOfferDB>>

    // Stars
    fun updateStarsDB(starsDB: List<StarDB>)
    fun insertStarsDB(starsDB: List<StarDB>)
    fun getStarsDBLiveData(): LiveData<List<StarDB>>

    // Offers
    fun updateOffersDB(offersDB: List<OfferDB>)
    fun insertOffersDB(offersDB: List<OfferDB>)
    fun getOffersDBLiveData(): LiveData<List<OfferDB>>
    fun updateOfferDB(offerDB : OfferDB)

    // Special Offers
    fun updateSpecialOffersDB(specialOffersDB: List<SpecialOfferDB>)
    fun insertSpecialOffersDB(specialOffersDB: List<SpecialOfferDB>)
    fun getSpecialOffersDBLiveData(): LiveData<List<SpecialOfferDB>>

    //profile
    fun updateProfileDB(profileDB: ProfileDB)
    fun insertProfileDB(profileDB: ProfileDB)
    fun getProfileDBLiveData(): LiveData<ProfileDB>
    fun getCollectedStarsCount(): LiveData<Int>
}