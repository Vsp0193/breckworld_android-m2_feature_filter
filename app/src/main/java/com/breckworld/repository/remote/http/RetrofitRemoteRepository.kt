package com.breckworld.repository.remote.http

import com.breckworld.repository.remote.http.api.Api
import com.breckworld.repository.remote.http.api.RetrofitCreator
import com.breckworld.repository.remote.http.model.*
import kotlinx.coroutines.Deferred

object RetrofitRemoteRepository : RemoteRepository {

    private val api: Api = RetrofitCreator.initApi()

    override fun login(body: LoginRequest): Deferred<LoginResponse> =
        api.login(body)

    override fun signUp(body: SignUpRequest): Deferred<SignUpResponse> =
        api.signUp(body)

    override fun getAllObjects(
        lat: Double?,
        lng: Double?,
        radius: Int?,
        filter: String?
    ): Deferred<AllObjectsResponse> =
        api.getAllObjects(lat, lng, radius, filter)

    override fun getProfile(): Deferred<ProfileResponse> = api.getProfile()

    override fun search(
        lat: Double?,
        lng: Double?,
        city: String?,
        page: Int?,
        term: String?
    ): Deferred<List<SearchResult>> =
        api.search(lat, lng, city, page, term)

    override fun collectStar(
        starId: Long,
        lat: Double,
        lng: Double,
        walletId: Long?,
        itemType: String
    ): Deferred<CollectStarResponse> =
        api.collectStar(starId, lat, lng, walletId, itemType)

    override fun updateProfile(body: UpdateProfileRequest): Deferred<UpdateProfileResponse> =
        api.updateProfile(body)

    override fun sendFeedback(body: ReportIssueRequest): Deferred<ReportIssueResponse> =
        api.sendFeedback(body)

    override fun redeem(id: Long, body: RedeemRequest): Deferred<RedeemResponse> =
        api.redeem(id, body)

    override fun collectOffer(offerId: Long, lat: Double, lng: Double): Deferred<CollectStarResponse> =
        api.collectOffer(offerId, lat, lng)

    override fun getSpecialOffers(): Deferred<List<Offer>> =
        api.getSpecialOffers()
}