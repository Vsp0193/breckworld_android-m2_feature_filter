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

class HistoryVM(private val repository: Repository2) : BaseViewModel2() {

    val historyLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = repository.historyLiveData
    val historyLoadMoreLiveData: LiveData<ResponseListener<GeoStoreLoadMoreModel>>
        get() = repository.historyLoadMoreLiveData
    val searchLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = repository.searchLiveData

    fun getHistoryApiData(selectedSorting: String, selectedFilters: String, lat: String, lng: String, access_token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.historyApiCall(selectedSorting, selectedFilters, lat, lng, access_token)
        }
    }

    fun getHistoryLoadMoreApiData(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.historyLoadMoreApiCall(url)
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