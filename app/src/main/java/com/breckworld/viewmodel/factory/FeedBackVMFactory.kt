package com.breckworld.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.breckworld.repository.Repository2
import com.breckworld.viewmodel.*

class FeedBackVMFactory(private val repositories: Repository2) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeedBackVM(repositories) as T
    }
}