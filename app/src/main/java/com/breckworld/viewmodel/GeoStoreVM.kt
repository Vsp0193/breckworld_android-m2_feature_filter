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

class GeoStoreVM(private val repository: Repository2) : BaseViewModel2() {

    val geoStoreLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = repository.geoStoreLiveData
    val geoStoreLoadMoreLiveData: LiveData<ResponseListener<GeoStoreLoadMoreModel>>
        get() = repository.geoStoreLoadMoreLiveData
    val searchLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = repository.searchLiveData

    fun getGeoStoreApiData(selectedSorting: String, selectedFilters: String, lat: String, lng: String, access_token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.geoStoreApiCall(selectedSorting, selectedFilters, lat, lng, access_token)
        }
    }

    fun getGeoStoreLoadMoreApiData(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.geoStoreLoadMoreApiCall(url)
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