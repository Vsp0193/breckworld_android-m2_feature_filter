package com.breckworld.utils

import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.breckworld.repository.remote.http.error.NoNetworkException
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import com.breckworld.App
import com.breckworld.extensions.fromJson
import com.breckworld.repository.remote.http.model.BaseResponse
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

object CoroutineHelper {

    @Suppress("FunctionName")
    inline fun CustomCoroutineExceptionHandler(crossinline handler: (CoroutineContext, Throwable) -> Unit, listener: ExceptionErrorListener): CoroutineExceptionHandler =
            object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
                override fun handleException(context: CoroutineContext, exception: Throwable) {
                    Log.d("Coroutine MainExHandler", "Error: CustomCoroutineExceptionHandler ${exception.message}")
                    when (exception) {
                        is HttpException -> {
                            val jsonModel: String? = exception.response().errorBody()?.string()
                            jsonModel?.let {
                                val baseResponse = Gson().fromJson<BaseResponse>(jsonModel)
                                Toast.makeText(App.applicationContext(), baseResponse.errorDescription, Toast.LENGTH_LONG).show()
                            }
                            Log.d("Coroutine MainExHandler", "Error: ${exception.message}")
                            if (exception.code() == 401) listener.onUnAuthorized()
                        }
                        is NoNetworkException -> {
                            listener.onNoInternet()
                        }
                    }
                    handler.invoke(context, exception)
                }
            }

    @Suppress("FunctionName")
    inline fun CustomCoroutineWithNoInternetExceptionHandler(crossinline handler: (CoroutineContext, Throwable) -> Unit, listener: ExceptionErrorListener): CoroutineExceptionHandler =
        object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
            override fun handleException(context: CoroutineContext, exception: Throwable) {
                Log.d("Coroutine MainExHandler", "Error: CustomCoroutineExceptionHandler ${exception.message}")
                when (exception) {
                    is HttpException -> {
                        val jsonModel: String? = exception.response().errorBody()?.string()
                        jsonModel?.let {
                            val baseResponse = Gson().fromJson<BaseResponse>(jsonModel)
                            Toast.makeText(App.applicationContext(), baseResponse.errorDescription, Toast.LENGTH_LONG).show()
                        }
                        Log.d("Coroutine MainExHandler", "Error: ${exception.message}")
                        if (exception.code() == 401) listener.onUnAuthorized()
                    }
                }
                handler.invoke(context, exception)
            }
        }
}