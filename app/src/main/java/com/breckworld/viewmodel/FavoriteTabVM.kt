package com.breckworld.viewmodel

import androidx.lifecycle.*
import com.breckworld.repository.Repository2
import com.breckworld.utils.BaseViewModel2
import com.breckworld.app.repository.ResponseListener
import com.breckworld.model.geostore.GeoStoreModel
import com.breckworld.model.geostoreloadmore.GeoStoreLoadMoreModel
import com.breckworld.model.home.HomeModel
import com.breckworld.model.userprofile.UserProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteTabVM(private val repository: Repository2) : BaseViewModel2() {

    val favouriteTabLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = repository.favouriteTabLiveData
    val favouriteTabLoadMoreLiveData: LiveData<ResponseListener<GeoStoreLoadMoreModel>>
        get() = repository.favouriteTabLoadMoreLiveData
    val searchLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = repository.searchLiveData

    fun getFavoriteTabApiData(selectedSorting: String, selectedFilters: String, lat: String, lng: String, access_token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.favouriteTabApiCall(selectedSorting, selectedFilters, lat, lng, access_token)
        }
    }

    fun getFavouriteTabLoadMoreApiData(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.favouriteTabLoadMoreApiCall(url)
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