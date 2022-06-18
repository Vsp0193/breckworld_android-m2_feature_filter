package com.breckworld.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.breckworld.repository.Repository2
import com.breckworld.viewmodel.HomeVM

class HomeVMFactory(private val repositories: Repository2) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeVM(repositories) as T
    }
}