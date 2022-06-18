package com.breckworld.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.breckworld.webservice.ApiInterface
import com.breckworld.app.repository.ResponseListener
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
import com.breckworld.util.AppUtil
import com.breckworld.util.NetworkUtil
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class Repository2(
    private val apiInterface: ApiInterface,
    private val applicationContext: Context
) {

    val loginMutableLiveData = MutableLiveData<ResponseListener<LoginModel>>()
    val signupMutableLiveData = MutableLiveData<ResponseListener<SignupModel>>()
    val forgotPasscodeMutableLiveData = MutableLiveData<ResponseListener<ForgotPasscodeModel>>()
    val homeMutableLiveData = MutableLiveData<ResponseListener<HomeModel>>()
    val userProfileMutableLiveData = MutableLiveData<ResponseListener<UserProfileModel>>()
    val mdsMutableLiveData = MutableLiveData<ResponseListener<MdsModel>>()
    val editProfileMutableLiveData = MutableLiveData<ResponseListener<EditProfileModel>>()
    val feedBackMutableLiveData = MutableLiveData<ResponseListener<FeedBackModel>>()
    val favMutableLiveData = MutableLiveData<ResponseListener<FavModel>>()
    val geoStoreMutableLiveData = MutableLiveData<ResponseListener<GeoStoreModel>>()
    val geoStoreLoadMoreMutableLiveData = MutableLiveData<ResponseListener<GeoStoreLoadMoreModel>>()
    val favouriteTabMutableLiveData = MutableLiveData<ResponseListener<GeoStoreModel>>()
    val favouriteTabLoadMoreMutableLiveData = MutableLiveData<ResponseListener<GeoStoreLoadMoreModel>>()
    val historyMutableLiveData = MutableLiveData<ResponseListener<GeoStoreModel>>()
    val historyLoadMoreMutableLiveData = MutableLiveData<ResponseListener<GeoStoreLoadMoreModel>>()
    val walletMutableLiveData = MutableLiveData<ResponseListener<GeoStoreModel>>()
    val walletLoadMoreMutableLiveData = MutableLiveData<ResponseListener<GeoStoreLoadMoreModel>>()
    val searchMutableLiveData = MutableLiveData<ResponseListener<GeoStoreModel>>()

    val loginLiveData: LiveData<ResponseListener<LoginModel>>
        get() = loginMutableLiveData
    val signupLiveData: LiveData<ResponseListener<SignupModel>>
        get() = signupMutableLiveData
    val forgotPasscodeLiveData: LiveData<ResponseListener<ForgotPasscodeModel>>
        get() = forgotPasscodeMutableLiveData
    val userProfileLiveData: LiveData<ResponseListener<UserProfileModel>>
        get() = userProfileMutableLiveData
    val homeLiveData: LiveData<ResponseListener<HomeModel>>
        get() = homeMutableLiveData
    val mdsLiveData: LiveData<ResponseListener<MdsModel>>
        get() = mdsMutableLiveData
    val editProfileLiveData: LiveData<ResponseListener<EditProfileModel>>
        get() = editProfileMutableLiveData
    val feedBackLiveData: LiveData<ResponseListener<FeedBackModel>>
        get() = feedBackMutableLiveData
    val favLiveData: LiveData<ResponseListener<FavModel>>
        get() = favMutableLiveData
    val geoStoreLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = geoStoreMutableLiveData
    val geoStoreLoadMoreLiveData: LiveData<ResponseListener<GeoStoreLoadMoreModel>>
        get() = geoStoreLoadMoreMutableLiveData
    val favouriteTabLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = favouriteTabMutableLiveData
    val favouriteTabLoadMoreLiveData: LiveData<ResponseListener<GeoStoreLoadMoreModel>>
        get() = favouriteTabLoadMoreMutableLiveData
    val historyLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = historyMutableLiveData
    val historyLoadMoreLiveData: LiveData<ResponseListener<GeoStoreLoadMoreModel>>
        get() = historyLoadMoreMutableLiveData
    val walletLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = walletMutableLiveData
    val walletLoadMoreLiveData: LiveData<ResponseListener<GeoStoreLoadMoreModel>>
        get() = walletLoadMoreMutableLiveData
    val searchLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = searchMutableLiveData

    /**
     * Call login api
     */
    fun loginApiCall(jsonMap: Map<String?, Any>) {
        //AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<LoginModel>? =
                    apiInterface.loginApiCall(jsonMap)
                call?.enqueue(object : Callback<LoginModel?> {

                    override fun onResponse(
                        call: Call<LoginModel?>,
                        response: Response<LoginModel?>
                    ) {
                        //AppUtil.hideProgressWithCheck()
                        loginMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<LoginModel?>, t: Throwable) {
                        //AppUtil.hideProgressWithCheck()
                        loginMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                //AppUtil.hideProgressWithCheck()
                loginMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            //AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call signup api
     */
    fun signupApiCall(jsonMap: Map<String?, Any>) {
        AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<SignupModel>? =
                    apiInterface.signupApiCall(jsonMap)
                call?.enqueue(object : Callback<SignupModel?> {

                    override fun onResponse(
                        call: Call<SignupModel?>,
                        response: Response<SignupModel?>
                    ) {
                        AppUtil.hideProgressWithCheck()
                        signupMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<SignupModel?>, t: Throwable) {
                        AppUtil.hideProgressWithCheck()
                        signupMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                AppUtil.hideProgressWithCheck()
                signupMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call forgot passcode api
     */
    fun forgotPasscodeApiCall(jsonMap: Map<String?, Any>) {
        AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<ForgotPasscodeModel>? =
                    apiInterface.forgotPasscodeApiCall(jsonMap)
                call?.enqueue(object : Callback<ForgotPasscodeModel?> {

                    override fun onResponse(
                        call: Call<ForgotPasscodeModel?>,
                        response: Response<ForgotPasscodeModel?>
                    ) {
                        AppUtil.hideProgressWithCheck()
                        forgotPasscodeMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<ForgotPasscodeModel?>, t: Throwable) {
                        AppUtil.hideProgressWithCheck()
                        forgotPasscodeMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                AppUtil.hideProgressWithCheck()
                forgotPasscodeMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call user profile api
     */
    fun getUserProfileApiCall(access_token: String?) {
        //AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<UserProfileModel>? =
                    apiInterface.userProfileApiCall(access_token)
                call?.enqueue(object : Callback<UserProfileModel?> {

                    override fun onResponse(
                        call: Call<UserProfileModel?>,
                        response: Response<UserProfileModel?>
                    ) {
                        //AppUtil.hideProgressWithCheck()
                        userProfileMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<UserProfileModel?>, t: Throwable) {
                        //AppUtil.hideProgressWithCheck()
                        userProfileMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                //AppUtil.hideProgressWithCheck()
                userProfileMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            //AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call home api
     */
    fun getHomeApiResponse(access_token: String?) {
        AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<HomeModel>? =
                    apiInterface.homeApiCall(access_token)
                call?.enqueue(object : Callback<HomeModel?> {

                    override fun onResponse(
                        call: Call<HomeModel?>,
                        response: Response<HomeModel?>
                    ) {
                        AppUtil.hideProgressWithCheck()
                        homeMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<HomeModel?>, t: Throwable) {
                        AppUtil.hideProgressWithCheck()
                        homeMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                AppUtil.hideProgressWithCheck()
                homeMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call mds api
     */
    fun getMdsApiCall(linkedMdsId: String?, slide: String?, access_token: String?) {
        AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                var call: Call<MdsModel>? = null

                if (slide.equals("")) {
                    call = apiInterface.getMDSApiCall(linkedMdsId.toString(), access_token)
                } else {
                    call = apiInterface.getMDSApiCall2(linkedMdsId.toString(), slide, access_token)
                }
                call?.enqueue(object : Callback<MdsModel?> {

                    override fun onResponse(
                        call: Call<MdsModel?>,
                        response: Response<MdsModel?>
                    ) {
                        AppUtil.hideProgressWithCheck()
                        mdsMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<MdsModel?>, t: Throwable) {
                        AppUtil.hideProgressWithCheck()
                        mdsMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                AppUtil.hideProgressWithCheck()
                mdsMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            AppUtil.hideProgressWithCheck()
        }
    }


    /**
     * Call edit profile api
     */
    fun editProfileApiCall(access_token: String, jsonMap: Map<String?, Any>) {
        AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<EditProfileModel>? =
                    apiInterface.editProfileApiCall(access_token, jsonMap)
                call?.enqueue(object : Callback<EditProfileModel?> {

                    override fun onResponse(
                        call: Call<EditProfileModel?>,
                        response: Response<EditProfileModel?>
                    ) {
                        AppUtil.hideProgressWithCheck()
                        editProfileMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<EditProfileModel?>, t: Throwable) {
                        AppUtil.hideProgressWithCheck()
                        editProfileMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                AppUtil.hideProgressWithCheck()
                editProfileMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call send feedback api
     */
    fun sendFeedBackApiCall(access_token: String, jsonMap: Map<String?, Any>) {
        AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<FeedBackModel>? =
                    apiInterface.sendFeedBackApiCall(access_token, jsonMap)
                call?.enqueue(object : Callback<FeedBackModel?> {

                    override fun onResponse(
                        call: Call<FeedBackModel?>,
                        response: Response<FeedBackModel?>
                    ) {
                        AppUtil.hideProgressWithCheck()
                        feedBackMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<FeedBackModel?>, t: Throwable) {
                        AppUtil.hideProgressWithCheck()
                        feedBackMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                AppUtil.hideProgressWithCheck()
                feedBackMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call fav api
     */
    fun favApiCall(linkedMdsId: String?, access_token: String?) {
        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<FavModel>? =
                    apiInterface.favApiCall(linkedMdsId.toString(), access_token)
                call?.enqueue(object : Callback<FavModel?> {

                    override fun onResponse(
                        call: Call<FavModel?>,
                        response: Response<FavModel?>
                    ) {
                        favMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<FavModel?>, t: Throwable) {
                        favMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                favMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {

        }
    }

    /**
     * Call geo store api
     */
    fun geoStoreApiCall(selectedSorting: String, selectedFilters: String, lat: String, lng: String, access_token: String?) {
        AppUtil.showProgress(applicationContext)
        if (NetworkUtil.networkStatus(applicationContext)) {
                try {
                    var call: Call<GeoStoreModel>? = null

                    if (!lat.equals("") && !lng.equals("") && !selectedFilters.equals("") && !selectedSorting.equals("")) {
                        call = apiInterface.getGeoStoreApiCall("sections/geostore", selectedSorting, selectedFilters, lat, lng, access_token)
                    } else if (!lat.equals("") && !lng.equals("") && !selectedFilters.equals("")) {
                        call = apiInterface.getGeoStoreApiCall("sections/geostore", selectedFilters, lat, lng, access_token)
                    } else if (!lat.equals("") && !lng.equals("") && !selectedSorting.equals("")) {
                        call = apiInterface.getGeoStoreApiCall4("sections/geostore", selectedSorting, lat, lng, access_token)
                    } else if (!lat.equals("") && !lng.equals("")) {
                        call = apiInterface.getGeoStoreApiCall3("sections/geostore", access_token, lat, lng)
                    } else if (selectedFilters.equals("")) {
                        call = apiInterface.getGeoStoreApiCall2("sections/geostore",selectedSorting, access_token)
                    } else if (selectedSorting.equals("")) {
                        call = apiInterface.getGeoStoreApiCall("sections/geostore", selectedFilters, access_token)
                    } else if (!selectedSorting.equals("") && !selectedFilters.equals("")) {
                        call = apiInterface.getGeoStoreApiCall("sections/geostore", selectedSorting, selectedFilters, access_token)
                    } else {
                        call = apiInterface.getGeoStoreApiCall("sections/geostore", access_token)
                    }
                    call?.enqueue(object : Callback<GeoStoreModel?> {

                        override fun onResponse(
                            call: Call<GeoStoreModel?>,
                            response: Response<GeoStoreModel?>
                        ) {
                            AppUtil.hideProgressWithCheck()
                            geoStoreMutableLiveData.postValue(ResponseListener.Success(response.body()))
                        }

                        override fun onFailure(call: Call<GeoStoreModel?>, t: Throwable) {
                            AppUtil.hideProgressWithCheck()
                            geoStoreMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                        }
                    })
                } catch (e: JSONException) {
                    e.printStackTrace()
                    AppUtil.hideProgressWithCheck()
                    geoStoreMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
                }
            } else {
                AppUtil.hideProgressWithCheck()
            }
    }

    /**
     * Call geo store load more api
     */
    fun geoStoreLoadMoreApiCall(url: String) {
        //AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<GeoStoreLoadMoreModel>? = apiInterface.getGeoStoreLoadMoreApiCall(url)
                call?.enqueue(object : Callback<GeoStoreLoadMoreModel?> {

                    override fun onResponse(
                        call: Call<GeoStoreLoadMoreModel?>,
                        response: Response<GeoStoreLoadMoreModel?>
                    ) {
                        //AppUtil.hideProgressWithCheck()
                        geoStoreLoadMoreMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<GeoStoreLoadMoreModel?>, t: Throwable) {
                        //AppUtil.hideProgressWithCheck()
                        geoStoreLoadMoreMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                //AppUtil.hideProgressWithCheck()
                geoStoreLoadMoreMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            //AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call favourite tab api
     */
    fun favouriteTabApiCall(selectedSorting: String, selectedFilters: String, lat: String, lng: String, access_token: String?) {
        AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                var call: Call<GeoStoreModel>? = null
                if (!lat.equals("") && !lng.equals("") && !selectedFilters.equals("") && !selectedSorting.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/favorite", selectedSorting, selectedFilters, lat, lng, access_token)
                } else if (!lat.equals("") && !lng.equals("") && !selectedFilters.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/favorite", selectedFilters, lat, lng, access_token)
                } else if (!lat.equals("") && !lng.equals("") && !selectedSorting.equals("")) {
                    call = apiInterface.getGeoStoreApiCall4("sections/favorite", selectedSorting, lat, lng, access_token)
                } else if (!lat.equals("") && !lng.equals("")) {
                    call = apiInterface.getGeoStoreApiCall3("sections/favorite", access_token, lat, lng)
                } else if (selectedFilters.equals("")) {
                    call = apiInterface.getGeoStoreApiCall2("sections/favorite",selectedSorting, access_token)
                } else if (selectedSorting.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/favorite", selectedFilters, access_token)
                } else if (!selectedSorting.equals("") && !selectedFilters.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/favorite", selectedSorting, selectedFilters, access_token)
                } else {
                    call = apiInterface.getGeoStoreApiCall("sections/favorite", access_token)
                }
                call?.enqueue(object : Callback<GeoStoreModel?> {

                    override fun onResponse(
                        call: Call<GeoStoreModel?>,
                        response: Response<GeoStoreModel?>
                    ) {
                        AppUtil.hideProgressWithCheck()
                        favouriteTabMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<GeoStoreModel?>, t: Throwable) {
                        AppUtil.hideProgressWithCheck()
                        favouriteTabMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                AppUtil.hideProgressWithCheck()
                favouriteTabMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call favourite tab load more api
     */
    fun favouriteTabLoadMoreApiCall(url: String) {
        //AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<GeoStoreLoadMoreModel>? = apiInterface.getGeoStoreLoadMoreApiCall(url)
                call?.enqueue(object : Callback<GeoStoreLoadMoreModel?> {

                    override fun onResponse(
                        call: Call<GeoStoreLoadMoreModel?>,
                        response: Response<GeoStoreLoadMoreModel?>
                    ) {
                        //AppUtil.hideProgressWithCheck()
                        favouriteTabLoadMoreMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<GeoStoreLoadMoreModel?>, t: Throwable) {
                        //AppUtil.hideProgressWithCheck()
                        favouriteTabLoadMoreMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                //AppUtil.hideProgressWithCheck()
                favouriteTabLoadMoreMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            //AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call history api
     */
    fun historyApiCall(selectedSorting: String, selectedFilters: String, lat: String, lng: String, access_token: String?) {
        AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                var call: Call<GeoStoreModel>? = null
                if (!lat.equals("") && !lng.equals("") && !selectedFilters.equals("") && !selectedSorting.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/history", selectedSorting, selectedFilters, lat, lng, access_token)
                } else if (!lat.equals("") && !lng.equals("") && !selectedFilters.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/history", selectedFilters, lat, lng, access_token)
                } else if (!lat.equals("") && !lng.equals("") && !selectedSorting.equals("")) {
                    call = apiInterface.getGeoStoreApiCall4("sections/history", selectedSorting, lat, lng, access_token)
                } else if (!lat.equals("") && !lng.equals("")) {
                    call = apiInterface.getGeoStoreApiCall3("sections/history", access_token, lat, lng)
                } else if (selectedFilters.equals("")) {
                    call = apiInterface.getGeoStoreApiCall2("sections/history",selectedSorting, access_token)
                } else if (selectedSorting.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/history", selectedFilters, access_token)
                } else if (!selectedSorting.equals("") && !selectedFilters.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/history", selectedSorting, selectedFilters, access_token)
                } else {
                    call = apiInterface.getGeoStoreApiCall("sections/history", access_token)
                }
                call?.enqueue(object : Callback<GeoStoreModel?> {

                    override fun onResponse(
                        call: Call<GeoStoreModel?>,
                        response: Response<GeoStoreModel?>
                    ) {
                        AppUtil.hideProgressWithCheck()
                        historyMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<GeoStoreModel?>, t: Throwable) {
                        AppUtil.hideProgressWithCheck()
                        historyMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                AppUtil.hideProgressWithCheck()
                historyMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call history load more api
     */
    fun historyLoadMoreApiCall(url: String) {
        //AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<GeoStoreLoadMoreModel>? = apiInterface.getGeoStoreLoadMoreApiCall(url)
                call?.enqueue(object : Callback<GeoStoreLoadMoreModel?> {

                    override fun onResponse(
                        call: Call<GeoStoreLoadMoreModel?>,
                        response: Response<GeoStoreLoadMoreModel?>
                    ) {
                        //AppUtil.hideProgressWithCheck()
                        historyLoadMoreMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<GeoStoreLoadMoreModel?>, t: Throwable) {
                        //AppUtil.hideProgressWithCheck()
                        historyLoadMoreMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                //AppUtil.hideProgressWithCheck()
                historyLoadMoreMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            //AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call wallet api
     */
    fun walletApiCall(selectedSorting: String, selectedFilters: String, lat: String, lng: String, access_token: String?) {
        AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                var call: Call<GeoStoreModel>? = null
                if (!lat.equals("") && !lng.equals("") && !selectedFilters.equals("") && !selectedSorting.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/wallet", selectedSorting, selectedFilters, lat, lng, access_token)
                } else if (!lat.equals("") && !lng.equals("") && !selectedFilters.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/wallet", selectedFilters, lat, lng, access_token)
                } else if (!lat.equals("") && !lng.equals("") && !selectedSorting.equals("")) {
                    call = apiInterface.getGeoStoreApiCall4("sections/wallet", selectedSorting, lat, lng, access_token)
                } else if (!lat.equals("") && !lng.equals("")) {
                    call = apiInterface.getGeoStoreApiCall3("sections/wallet", access_token, lat, lng)
                } else if (selectedFilters.equals("")) {
                    call = apiInterface.getGeoStoreApiCall2("sections/wallet",selectedSorting, access_token)
                } else if (selectedSorting.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/wallet", selectedFilters, access_token)
                } else if (!selectedSorting.equals("") && !selectedFilters.equals("")) {
                    call = apiInterface.getGeoStoreApiCall("sections/wallet", selectedSorting, selectedFilters, access_token)
                } else {
                    call = apiInterface.getGeoStoreApiCall("sections/wallet", access_token)
                }
                call?.enqueue(object : Callback<GeoStoreModel?> {

                    override fun onResponse(
                        call: Call<GeoStoreModel?>,
                        response: Response<GeoStoreModel?>
                    ) {
                        AppUtil.hideProgressWithCheck()
                        walletMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<GeoStoreModel?>, t: Throwable) {
                        AppUtil.hideProgressWithCheck()
                        walletMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                AppUtil.hideProgressWithCheck()
                walletMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call wallet load more api
     */
    fun walletLoadMoreApiCall(url: String) {
        //AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<GeoStoreLoadMoreModel>? = apiInterface.getGeoStoreLoadMoreApiCall(url)
                call?.enqueue(object : Callback<GeoStoreLoadMoreModel?> {

                    override fun onResponse(
                        call: Call<GeoStoreLoadMoreModel?>,
                        response: Response<GeoStoreLoadMoreModel?>
                    ) {
                        //AppUtil.hideProgressWithCheck()
                        walletLoadMoreMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<GeoStoreLoadMoreModel?>, t: Throwable) {
                        //AppUtil.hideProgressWithCheck()
                        walletLoadMoreMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                //AppUtil.hideProgressWithCheck()
                walletLoadMoreMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            //AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call search api
     */
    fun searchApiCall(rws: String, keyword: String, selectedFilters: String, selectedSorting: String, lat: String, lng: String, access_token: String?) {
        //AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                var call: Call<GeoStoreModel>? = null
                if(rws.equals("")) {
                    call = apiInterface.searchApiCall(
                        keyword,
                        selectedFilters,
                        selectedSorting,
                        lat,
                        lng,
                        access_token
                    )
                } else {
                    call = apiInterface.searchApiCall(
                        rws,
                        keyword,
                        selectedFilters,
                        selectedSorting,
                        lat,
                        lng,
                        access_token)
                }
                call?.enqueue(object : Callback<GeoStoreModel?> {

                    override fun onResponse(
                        call: Call<GeoStoreModel?>,
                        response: Response<GeoStoreModel?>
                    ) {
                        //AppUtil.hideProgressWithCheck()
                        searchMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<GeoStoreModel?>, t: Throwable) {
                        //AppUtil.hideProgressWithCheck()
                        searchMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                //AppUtil.hideProgressWithCheck()
                searchMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            //AppUtil.hideProgressWithCheck()
        }
    }

    /**
     * Call search load more api
     */
    fun searchLoadMoreApiCall(url: String) {
        //AppUtil.showProgress(applicationContext)

        if (NetworkUtil.networkStatus(applicationContext)) {
            try {
                val call: Call<GeoStoreLoadMoreModel>? = apiInterface.getGeoStoreLoadMoreApiCall(url)
                call?.enqueue(object : Callback<GeoStoreLoadMoreModel?> {

                    override fun onResponse(
                        call: Call<GeoStoreLoadMoreModel?>,
                        response: Response<GeoStoreLoadMoreModel?>
                    ) {
                        //AppUtil.hideProgressWithCheck()
                        geoStoreLoadMoreMutableLiveData.postValue(ResponseListener.Success(response.body()))
                    }

                    override fun onFailure(call: Call<GeoStoreLoadMoreModel?>, t: Throwable) {
                        //AppUtil.hideProgressWithCheck()
                        geoStoreLoadMoreMutableLiveData.postValue(ResponseListener.Failure(t.message.toString()))
                    }
                })
            } catch (e: JSONException) {
                e.printStackTrace()
                //AppUtil.hideProgressWithCheck()
                geoStoreLoadMoreMutableLiveData.postValue(ResponseListener.Error(e.message.toString()))
            }
        } else {
            //AppUtil.hideProgressWithCheck()
        }
    }

}