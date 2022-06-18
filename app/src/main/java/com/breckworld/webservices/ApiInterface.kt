package com.breckworld.webservice

import com.breckworld.model.editprofile.EditProfileModel
import com.breckworld.model.favourite.FavModel
import com.breckworld.model.forgotpasscode.ForgotPasscodeModel
import com.breckworld.model.geostore.GeoStoreModel
import com.breckworld.model.geostoreloadmore.GeoStoreLoadMoreModel
import com.breckworld.model.home.HomeModel
import com.breckworld.model.login.LoginModel
import com.breckworld.model.mds.MdsModel
import com.breckworld.model.search.SearchModel
import com.breckworld.model.sendfeedback.FeedBackModel
import com.breckworld.model.signup.SignupModel
import com.breckworld.model.userprofile.UserProfileModel
import retrofit2.Call
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiInterface {

    @POST("oauth/token")
    fun loginApiCall(@Body request: Map<String?, Any?>?): Call<LoginModel>?

    @POST("signup")
    fun signupApiCall(@Body request: Map<String?, Any?>?): Call<SignupModel>?

    @POST("profile/passcode")
    fun forgotPasscodeApiCall(@Body request: Map<String?, Any?>?): Call<ForgotPasscodeModel>?

    @GET("profile")
    fun userProfileApiCall(@Query("access_token") access_token: String?): Call<UserProfileModel>?

    @GET("sections/home")
    fun homeApiCall(@Query("access_token") access_token: String?): Call<HomeModel>?

    @GET("mds/{linked_mds}")
    fun getMDSApiCall(@Path("linked_mds") linkedMdsId : String, @Query("access_token") access_token: String?): Call<MdsModel>?

    @GET("mds/{linked_mds}")
    fun getMDSApiCall2(@Path("linked_mds") linkedMdsId : String, @Query("slide") slide: String?, @Query("access_token") access_token: String?): Call<MdsModel>?

    @POST("profile/update")
    fun editProfileApiCall(@Query("access_token") access_token: String?, @Body request: Map<String?, Any?>?): Call<EditProfileModel>?

    @POST("feedback")
    fun sendFeedBackApiCall(@Query("access_token") access_token: String?, @Body request: Map<String?, Any?>?): Call<FeedBackModel>?

    @POST("sections/favorite/{linked_mds}")
    fun favApiCall(@Path("linked_mds") linkedMdsId : String, @Query("access_token") access_token: String?): Call<FavModel>?

    @GET
    fun getGeoStoreApiCall(@Url path: String, @Query("access_token") access_token: String?): Call<GeoStoreModel>?

    @GET
    fun getGeoStoreApiCall(@Url path: String, @Query("filters") filters: String, @Query("access_token") access_token: String?): Call<GeoStoreModel>?

    @GET
    fun getGeoStoreApiCall(@Url path: String, @Query("sort") sort: String, @Query("filters") filters: String, @Query("access_token") access_token: String?): Call<GeoStoreModel>?

    @GET
    fun getGeoStoreApiCall2(@Url path: String, @Query("sort") sort: String, @Query("access_token") access_token: String?): Call<GeoStoreModel>?

    @GET
    fun getGeoStoreApiCall(@Url path: String,  @Query("filters") filters: String, @Query("lat") lat: String, @Query("long") long: String, @Query("access_token") access_token: String?): Call<GeoStoreModel>?

    @GET
    fun getGeoStoreApiCall(@Url path: String, @Query("sort") sort: String, @Query("filters") filters: String, @Query("lat") lat: String, @Query("long") long: String, @Query("access_token") access_token: String?): Call<GeoStoreModel>?

    @GET
    fun getGeoStoreApiCall3(@Url path: String, @Query("access_token") access_token: String?,
                           @Query("lat") lat: String, @Query("long") long: String): Call<GeoStoreModel>?

    @GET
    fun getGeoStoreApiCall4(@Url path: String, @Query("sort") sort: String, @Query("lat") lat: String, @Query("long") long: String, @Query("access_token") access_token: String?): Call<GeoStoreModel>?

    @GET
    fun getGeoStoreLoadMoreApiCall(@Url path: String): Call<GeoStoreLoadMoreModel>?

    @GET("sections/search")
    fun searchApiCall(@Query("keyword") keyword: String, @Query("filters") filters: String, @Query("sort") sort: String, @Query("lat") lat: String, @Query("long") long: String, @Query("access_token") access_token: String?): Call<GeoStoreModel>?

    @GET("sections/search")
    fun searchApiCall(@Query("rws") rws: String, @Query("keyword") keyword: String, @Query("filters") filters: String, @Query("sort") sort: String, @Query("lat") lat: String, @Query("long") long: String, @Query("access_token") access_token: String?): Call<GeoStoreModel>?

}