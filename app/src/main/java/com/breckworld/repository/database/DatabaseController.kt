package com.breckworld.repository.database

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.room.*
import com.breckworld.repository.database.model.*

/**
 * @author Dmytro Bondarenko
 *         Date: 03.06.2019
 *         Time: 13:03
 *         E-mail: bondes87@gmail.com
 */

@Dao
abstract class DatabaseController : DatabaseRepository {

    // Table Caching
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insertTableCacheData(data: TableCache)

    @Query("SELECT * FROM TableCache WHERE tableName = :tableName")
    abstract override fun getTableCacheData(tableName: String): List<TableCache>

    // Location Stars
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insertLocationStarsDB(locationStarsDB: List<LocationStarDB>)

    @Query("DELETE FROM LocationStarDB")
    abstract fun clearLocationStarsDB()

    @Transaction
    override fun updateLocationStarsDB(locationStarsDB: List<LocationStarDB>, location: Location) {
        clearLocationStarsDB()
        insertLocationStarsDB(locationStarsDB)
        val currentTime = System.currentTimeMillis()
        insertTableCacheData(
            TableCache(
                TableNames.LocationStar,
                currentTime,
                location.latitude,
                location.longitude,
                false
            )
        )
    }

    @Query("SELECT * FROM LocationStarDB")
    abstract override fun getLocationStarsDBLiveData(): LiveData<List<LocationStarDB>>

    @Query(
        """
            SELECT * 
            FROM LocationStarDB
            WHERE LocationStarDB.id NOT IN
            (SELECT entryId FROM StarDB)
        """
    )
    abstract override fun getLocationStarsWithoutCollectedLiveData(): LiveData<List<LocationStarDB>>

    // Location Offers
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insertLocationOffersDB(locationOffersDB: List<LocationOfferDB>)

    @Query("DELETE FROM LocationOfferDB")
    abstract fun clearLocationOffersDB()

    @Transaction
    override fun updateLocationOffersDB(locationOffersDB: List<LocationOfferDB>, location: Location) {
        clearLocationOffersDB()
        insertLocationOffersDB(locationOffersDB)
        val currentTime = System.currentTimeMillis()
        insertTableCacheData(
            TableCache(
                TableNames.LocationOffer,
                currentTime,
                location.latitude,
                location.longitude,
                false
            )
        )
    }

    @Query("SELECT * FROM LocationOfferDB")
    abstract override fun getLocationOffersDBLiveData(): LiveData<List<LocationOfferDB>>

    // Stars
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insertStarsDB(starsDB: List<StarDB>)

    @Query("DELETE FROM StarDB")
    abstract fun clearStars()

    @Transaction
    override fun updateStarsDB(starsDB: List<StarDB>) {
        clearStars()
        insertStarsDB(starsDB)
        val currentTime = System.currentTimeMillis()
        insertTableCacheData(TableCache(TableNames.Star, currentTime, 0.0, 0.0, false))
    }

    @Query("SELECT * FROM StarDB")
    abstract override fun getStarsDBLiveData(): LiveData<List<StarDB>>

    @Query("SELECT COUNT(entryId) FROM StarDB")
    abstract override fun getCollectedStarsCount(): LiveData<Int>

    // Offers
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insertOffersDB(offersDB: List<OfferDB>)

    @Query("DELETE FROM OfferDB")
    abstract fun clearOffers()

    @Transaction
    override fun updateOffersDB(offersDB: List<OfferDB>) {
        clearOffers()
        insertOffersDB(offersDB)
        val currentTime = System.currentTimeMillis()
        insertTableCacheData(TableCache(TableNames.Offer, currentTime, 0.0, 0.0, false))
    }

    @Query("SELECT * FROM OfferDB")
    abstract override fun getOffersDBLiveData(): LiveData<List<OfferDB>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun updateOfferDB(offerDB: OfferDB)

    // Special Offers
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insertSpecialOffersDB(specialOffersDB: List<SpecialOfferDB>)

    @Query("DELETE FROM SpecialOfferDB")
    abstract fun clearSpecialOffers()

    @Transaction
    override fun updateSpecialOffersDB(specialOffersDB: List<SpecialOfferDB>) {
        clearSpecialOffers()
        insertSpecialOffersDB(specialOffersDB)
        val currentTime = System.currentTimeMillis()
        insertTableCacheData(TableCache(TableNames.SpecialOffer, currentTime, 0.0, 0.0, false))
    }

    @Query("SELECT * FROM SpecialOfferDB")
    abstract override fun getSpecialOffersDBLiveData(): LiveData<List<SpecialOfferDB>>

    // Profile
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insertProfileDB(profileDB: ProfileDB)

    @Query("DELETE FROM ProfileDB")
    abstract fun clearProfile()

    @Transaction
    override fun updateProfileDB(profileDB: ProfileDB) {
        clearProfile()
        insertProfileDB(profileDB)
        val currentTime = System.currentTimeMillis()
        insertTableCacheData(TableCache(TableNames.Profile, currentTime, 0.0, 0.0, false))
    }

    @Query("SELECT * FROM ProfileDB")
    abstract override fun getProfileDBLiveData(): LiveData<ProfileDB>
}