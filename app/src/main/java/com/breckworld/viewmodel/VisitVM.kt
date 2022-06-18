package com.breckworld.viewmodel

import androidx.lifecycle.*
import com.breckworld.repository.Repository2
import com.breckworld.utils.BaseViewModel2
import com.breckworld.app.repository.ResponseListener
import com.breckworld.model.favourite.FavModel
import com.breckworld.model.mds.MdsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VisitVM(private val repository: Repository2) : BaseViewModel2() {

    val mdsLiveData: LiveData<ResponseListener<MdsModel>>
        get() = repository.mdsLiveData

    val favLiveData: LiveData<ResponseListener<FavModel>>
        get() = repository.favLiveData

    fun getMdsApiData(linkedMdsId: String?, slide: String?, access_token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMdsApiCall(linkedMdsId, slide, access_token)
        }
    }

    fun favApiCall(linkedMdsId: String?, access_token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.favApiCall(linkedMdsId, access_token)
        }
    }

}