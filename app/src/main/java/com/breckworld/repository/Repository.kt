package com.breckworld.repository

import android.location.Location
import androidx.lifecycle.LiveData
import com.breckworld.repository.database.AppDatabase
import com.breckworld.repository.database.DatabaseCreator
import com.breckworld.repository.database.DatabaseRepository
import com.breckworld.repository.database.model.*
import com.breckworld.repository.remote.http.RemoteRepository
import com.breckworld.repository.remote.http.RetrofitRemoteRepository
import com.breckworld.repository.remote.http.model.*
import com.breckworld.repository.settings.SettingsRepository
import com.breckworld.repository.settings.SharedPreferencesSettingsRepository
import kotlinx.coroutines.Deferred

object Repository : RemoteRepository, SettingsRepository, DatabaseRepository {

    private val remoteRepository: RemoteRepository = RetrofitRemoteRepository
    private val settingsRepository: SettingsRepository = SharedPreferencesSettingsRepository
    private val database: AppDatabase = DatabaseCreator.initDatabase()
    private val databaseRepository: DatabaseRepository = database.databaseRepository()
    val databaseExecutor = database.queryExecutor

    // remoteRepository
    override fun login(body: LoginRequest): Deferred<LoginResponse> =
        remoteRepository.login(body)

    override fun signUp(body: SignUpRequest): Deferred<SignUpResponse> =
        remoteRepository.signUp(body)

    override fun getAllObjects(
        lat: Double?,
        lng: Double?,
        radius: Int?,
        filter: String?
    ): Deferred<AllObjectsResponse> =
        remoteRepository.getAllObjects(lat, lng, radius, filter)

    override fun getProfile(): Deferred<ProfileResponse> =
        remoteRepository.getProfile()

    override fun search(
        lat: Double?,
        lng: Double?,
        city: String?,
        page: Int?,
        term: String?
    ): Deferred<List<SearchResult>> =
        remoteRepository.search(lat, lng, city, page, term)

    override fun updateProfile(body: UpdateProfileRequest): Deferred<UpdateProfileResponse> =
        remoteRepository.updateProfile(body)

    override fun sendFeedback(body: ReportIssueRequest): Deferred<ReportIssueResponse> =
        remoteRepository.sendFeedback(body)

    override fun collectStar(
        starId: Long,
        lat: Double,
        lng: Double,
        walletId: Long?,
        itemType: String
    ): Deferred<CollectStarResponse> =
        remoteRepository.collectStar(starId, lat, lng, walletId, itemType)

    override fun redeem(id: Long, body: RedeemRequest): Deferred<RedeemResponse> =
        remoteRepository.redeem(id, body)

    override fun collectOffer(offerId: Long, lat: Double, lng: Double): Deferred<CollectStarResponse> =
        remoteRepository.collectOffer(offerId, lat, lng)

    override fun getSpecialOffers(): Deferred<List<Offer>> =
        remoteRepository.getSpecialOffers()

    // settingsRepository
    override fun getToken(): String {
        return settingsRepository.getToken()
    }

    override fun saveToken(token: String) {
        settingsRepository.saveToken(token)
    }

    override fun removeToken() {
        settingsRepository.removeToken()
    }

    override fun getExpiresTime(): Long {
        return settingsRepository.getExpiresTime()
    }

    override fun saveExpiresTime(expiresTime: Long) {
        settingsRepository.saveExpiresTime(expiresTime)
    }

    override fun removeExpiresTime() {
        settingsRepository.removeExpiresTime()
    }

    fun logout() {
        removeToken()
        removeExpiresTime()
    }

    override fun clear() {
        settingsRepository.clear()
    }

    override var isCombined: Boolean
        get() = settingsRepository.isCombined
        set(value) {
            settingsRepository.isCombined = value
        }

    override fun getAccessToken(): String = settingsRepository.getAccessToken()

    override fun isGuideHome(): Boolean =
        settingsRepository.isGuideHome()

    override fun isGuideSearch(): Boolean =
        settingsRepository.isGuideSearch()

    override fun isGuideArView(): Boolean =
        settingsRepository.isGuideArView()

    override fun isGuideStars(): Boolean =
        settingsRepository.isGuideStars()

    override fun isGuideWallet(): Boolean =
        settingsRepository.isGuideWallet()

    override fun saveGuideHome(isGuideShow: Boolean) =
        settingsRepository.saveGuideHome(isGuideShow)

    override fun saveGuideSearch(isGuideShow: Boolean) =
        settingsRepository.saveGuideSearch(isGuideShow)

    override fun saveGuideArView(isGuideShow: Boolean) =
        settingsRepository.saveGuideArView(isGuideShow)

    override fun saveGuideStars(isGuideShow: Boolean) =
        settingsRepository.saveGuideStars(isGuideShow)

    override fun saveGuideWallet(isGuideShow: Boolean) =
        settingsRepository.saveGuideWallet(isGuideShow)

    override fun startGuide() =
        settingsRepository.startGuide()

    override fun isFirstRun(): Boolean =
        settingsRepository.isFirstRun()

    override fun saveFirstRun(isFirstRun: Boolean) =
        settingsRepository.saveFirstRun(isFirstRun)

    //databaseRepository
    override fun insertTableCacheData(data: TableCache) =
        databaseRepository.insertTableCacheData(data)

    override fun getTableCacheData(tableName: String): List<TableCache> =
        databaseRepository.getTableCacheData(tableName)

    override fun updateLocationStarsDB(locationStarsDB: List<LocationStarDB>, location: Location) =
        databaseRepository.updateLocationStarsDB(locationStarsDB, location)

    override fun insertLocationStarsDB(locationStarsDB: List<LocationStarDB>) =
        databaseRepository.insertLocationStarsDB(locationStarsDB)

    override fun getLocationStarsDBLiveData(): LiveData<List<LocationStarDB>> =
        databaseRepository.getLocationStarsDBLiveData()

    override fun getLocationStarsWithoutCollectedLiveData(): LiveData<List<LocationStarDB>> =
        databaseRepository.getLocationStarsWithoutCollectedLiveData()

    override fun updateLocationOffersDB(locationOffersDB: List<LocationOfferDB>, location: Location) =
        databaseRepository.updateLocationOffersDB(locationOffersDB, location)

    override fun insertLocationOffersDB(locationOffersDB: List<LocationOfferDB>) =
        databaseRepository.insertLocationOffersDB(locationOffersDB)

    override fun getLocationOffersDBLiveData(): LiveData<List<LocationOfferDB>> =
        databaseRepository.getLocationOffersDBLiveData()

    override fun updateStarsDB(starsDB: List<StarDB>) =
        databaseRepository.updateStarsDB(starsDB)

    override fun insertStarsDB(starsDB: List<StarDB>) =
        databaseRepository.insertStarsDB(starsDB)

    override fun getStarsDBLiveData(): LiveData<List<StarDB>> =
        databaseRepository.getStarsDBLiveData()

    override fun getCollectedStarsCount(): LiveData<Int> =
        databaseRepository.getCollectedStarsCount()

    override fun updateOffersDB(offersDB: List<OfferDB>) =
        databaseRepository.updateOffersDB(offersDB)

    override fun insertOffersDB(offersDB: List<OfferDB>) =
        databaseRepository.insertOffersDB(offersDB)

    override fun getOffersDBLiveData(): LiveData<List<OfferDB>> =
        databaseRepository.getOffersDBLiveData()

    override fun updateOfferDB(offerDB: OfferDB) =
        databaseRepository.updateOfferDB(offerDB)

    override fun updateSpecialOffersDB(specialOffersDB: List<SpecialOfferDB>) =
        databaseRepository.updateSpecialOffersDB(specialOffersDB)

    override fun insertSpecialOffersDB(specialOffersDB: List<SpecialOfferDB>) =
        databaseRepository.insertSpecialOffersDB(specialOffersDB)

    override fun getSpecialOffersDBLiveData(): LiveData<List<SpecialOfferDB>> =
        databaseRepository.getSpecialOffersDBLiveData()

    override fun getProfileDBLiveData(): LiveData<ProfileDB> =
        databaseRepository.getProfileDBLiveData()

    override fun updateProfileDB(profileDB: ProfileDB) =
        databaseRepository.updateProfileDB(profileDB)

    override fun insertProfileDB(profileDB: ProfileDB) =
        databaseRepository.insertProfileDB(profileDB)

    fun clearAllTables() {
        database.clearAllTables()
    }
}