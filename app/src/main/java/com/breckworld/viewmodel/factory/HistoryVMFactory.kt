package com.breckworld.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.breckworld.repository.Repository2
import com.breckworld.viewmodel.FavoriteTabVM
import com.breckworld.viewmodel.GeoStoreVM
import com.breckworld.viewmodel.HistoryVM
import com.breckworld.viewmodel.HomeVM

class HistoryVMFactory(private val repositories: Repository2) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryVM(repositories) as T
    }
}