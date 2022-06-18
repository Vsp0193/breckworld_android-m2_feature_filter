package com.breckworld.repository.remote.http.api

import com.breckworld.repository.remote.http.model.*
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface Api {
    @POST("api/v1.0/oauth/token")
    fun login(@Body body: LoginRequest): Deferred<LoginResponse>

    @POST("api/v1.0/signup")
    fun signUp(@Body body: SignUpRequest): Deferred<SignUpResponse>

    @GET("api/v1.0/items")
    fun getAllObjects(
        @Query("lat") lat: Double?,
        @Query("lng") lng: Double?,
        @Query("radius") radius: Int?,
        @Query("filter") filter: String?
    ): Deferred<AllObjectsResponse>

    @GET("api/v1.0/profile")
    fun getProfile(): Deferred<ProfileResponse>

    @GET("api/v1.0/items/search")
    fun search(
        @Query("lat") lat: Double?,
        @Query("lng") lng: Double?,
        @Query("city") city: String?,
        @Query("page") page: Int?,
        @Query("term") term: String?
    ): Deferred<List<SearchResult>>

    @FormUrlEncoded
    @PUT("api/v1.0/stars/{id}")
    fun collectStar(
        @Path("id") starId: Long,
        @Field("lat") lat: Double,
        @Field("lng") lng: Double,
        @Field("wallet_id") walletId: Long?,
        @Field("item_type") itemType: String
    ): Deferred<CollectStarResponse>

    @FormUrlEncoded
    @PUT("api/v1.0/offers/{id}")
    fun collectOffer(
        @Path("id") offerId: Long,
        @Field("lat") lat: Double,
        @Field("lng") lng: Double
    ): Deferred<CollectStarResponse>

    @GET("api/v1.0/special")
    fun getSpecialOffers(): Deferred<List<Offer>>

    @POST("api/v1.0/profile")
    fun updateProfile(@Body body: UpdateProfileRequest): Deferred<UpdateProfileResponse>

    @POST("api/v1.0/feedback")
    fun sendFeedback(@Body body: ReportIssueRequest): Deferred<ReportIssueResponse>

    @PUT("api/v1.0/redeem/{id}")
    fun redeem(@Path("id") id: Long, @Body body: RedeemRequest): Deferred<RedeemResponse>
}