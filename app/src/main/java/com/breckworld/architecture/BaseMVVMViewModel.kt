package com.breckworld.architecture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.breckworld.App
import com.breckworld.extensions.toast
import com.breckworld.livedata.Event
import com.breckworld.livedata.SingleLiveEvent
import com.breckworld.repository.Repository
import com.breckworld.utils.CoroutineHelper.CustomCoroutineExceptionHandler
import com.breckworld.utils.CoroutineHelper.CustomCoroutineWithNoInternetExceptionHandler
import com.breckworld.utils.ExceptionErrorListener
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseMVVMViewModel<L : Enum<L>> : ViewModel(), CoroutineScope {

    protected val repository = Repository
    //protected val compositeDisposable = CompositeDisposable()
    val progressVisibility = SingleLiveEvent<Boolean>()
    val logout = SingleLiveEvent<Boolean>()
    val noInternet = SingleLiveEvent<Boolean>()

    // coroutine
    private var job: Job = SupervisorJob()
    //    private val exceptionHandler = CoroutineHelper.exceptionHandler
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    // events liveData
    protected val _eventsLiveData: MutableLiveData<Event<L>> = MutableLiveData()
    val eventsLiveData: LiveData<Event<L>>
        get() {
            return _eventsLiveData
        }

    override fun onCleared() {
        super.onCleared()
        //cancel coroutine
        coroutineContext.cancelChildren()
    }

    val exceptionErrorListener = object : ExceptionErrorListener {
        override fun onNoInternet() {
            noInternet.value = true
        }

        override fun onUnAuthorized() {
            logout()
        }
    }

    fun launchCoroutine(block: suspend () -> Unit, handler: (CoroutineContext, Throwable) -> Unit): Job {
        val exceptionHandler = CustomCoroutineExceptionHandler(handler, exceptionErrorListener)
        return launch(exceptionHandler) {
            //            withContext(Dispatchers.IO) {
//                // background thread
//                block.invoke()
//            }
            block.invoke()
        }
    }

    fun launchCoroutineWithNoInternet(block: suspend () -> Unit, handler: (CoroutineContext, Throwable) -> Unit): Job {
        val exceptionHandler = CustomCoroutineWithNoInternetExceptionHandler(handler, exceptionErrorListener)
        return launch(exceptionHandler) {
            block.invoke()
        }
    }

    protected fun showLoading() {
        progressVisibility.postValue(true)
    }

    protected fun hideLoading() {
        progressVisibility.postValue(false)
    }

    fun logout() {
        launchCoroutine({
            withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
                repository.clearAllTables()
            }
        }, { _, throwable ->
            Log.d("DATABASE", "Error delete database ${throwable.message}")
        })
        repository.clear()
        logout.postValue(true)
    }

    protected fun showError(
        message: String?,
        cancelListener: (() -> Unit)? = null,
        retryListener: (() -> Unit)? = null
    ) {
        // if no dialog show toast
        message?.let {
            App.applicationContext().toast(it)
        }
    }
}
