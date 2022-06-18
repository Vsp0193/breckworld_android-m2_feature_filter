package com.breckworld.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.breckworld.repository.Repository2
import com.breckworld.viewmodel.HomeVM
import com.breckworld.viewmodel.LoginVM

class LoginVMFactory(private val repositories: Repository2) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginVM(repositories) as T
    }
}