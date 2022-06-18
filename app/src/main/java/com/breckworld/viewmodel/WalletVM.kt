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

class WalletVM(private val repository: Repository2) : BaseViewModel2() {

    val walletLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = repository.walletLiveData
    val walletLoadMoreLiveData: LiveData<ResponseListener<GeoStoreLoadMoreModel>>
        get() = repository.walletLoadMoreLiveData
    val searchLiveData: LiveData<ResponseListener<GeoStoreModel>>
        get() = repository.searchLiveData

    fun getWalletApiData(selectedSorting: String, selectedFilters: String, lat: String, lng: String, access_token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.walletApiCall(selectedSorting, selectedFilters, lat, lng, access_token)
        }
    }

    fun getWalletLoadMoreApiData(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.walletLoadMoreApiCall(url)
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