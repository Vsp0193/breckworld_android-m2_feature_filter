package com.breckworld.viewmodel

import androidx.lifecycle.*
import com.breckworld.repository.Repository2
import com.breckworld.utils.BaseViewModel2
import com.breckworld.app.repository.ResponseListener
import com.breckworld.model.geostore.GeoStoreModel
import com.breckworld.model.geostoreloadmore.GeoStoreLoadMoreModel
import com.breckworld.model.home.HomeModel
import com.breckworld.model.search.SearchModel
import com.breckworld.model.userprofile.UserProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeVM(private val repository: Repository2) : BaseViewModel2() {

    val homeLiveData: LiveData<ResponseListener<HomeModel>>
        get() = repository.homeLiveData
    val userProfileLiveData: LiveData<ResponseListener<UserProfileModel>>
        get() = repository.userProfileLiveData
    val searchLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = repository.searchLiveData
    val geoStoreLoadMoreLiveData: LiveData<ResponseListener<GeoStoreLoadMoreModel>>
        get() = repository.geoStoreLoadMoreLiveData

    fun getHomeApiData(access_token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getHomeApiResponse(access_token)
        }
    }

    fun getUserProfileApi(access_token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserProfileApiCall(access_token)
        }
    }

    fun searchApiData(rws: String, keyword: String, selectedFilters: String, selectedSorting: String, lat: String, lng: String, access_token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchApiCall(rws,
                keyword,
                selectedFilters,
                selectedSorting,
                lat,
                lng,
                access_token)
        }
    }

    fun searchLoadMoreApiData(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.geoStoreLoadMoreApiCall(url)
        }
    }
}