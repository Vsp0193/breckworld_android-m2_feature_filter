package com.breckworld.repository.remote.http.error

import androidx.annotation.StringRes
import androidx.core.util.Consumer
import retrofit2.HttpException
import com.breckworld.App
import com.breckworld.BuildConfig
import com.breckworld.R
import timber.log.Timber
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class GeneralErrorHandler(private val onFailure: ((CommonThrowable) -> Unit)? = null) : Consumer<Throwable> {

    private fun isNetworkError(throwable: Throwable) = throwable is SocketException || throwable is UnknownHostException || throwable is SocketTimeoutException

    override fun accept(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Timber.e(throwable)
        }
        val error = when {
            isNetworkError(throwable) -> CommonThrowable(getString(R.string.general_error_handler_server_error))
            throwable is NoNetworkException -> CommonThrowable(throwable.message!!)
            throwable is HttpException -> {
                CommonThrowable(getString(R.string.general_error_handler_unknown_error))

            }
            else -> CommonThrowable(getString(R.string.general_error_handler_unknown_error))
        }
        onFailure?.invoke(error)
    }

    private fun getFirstErrorText(errorsMap: HashMap<String, List<String>>?): String {
        var errorMessage = ""
        errorsMap?.let {
            for ((_, v) in it) {
                if (v.isNotEmpty()) {
                    errorMessage = v[0]
                    return@let
                }
            }
        }
        return errorMessage
    }

    private fun getString(@StringRes resId: Int) = App.applicationContext().getString(resId)

    companion object {
        const val ERROR_CODE_NO_ERRORS = 0
        const val ERROR_CODE_400 = 400 // Bad Request -- Validation errors.
        //        const val ERROR_CODE_401 = 401 // Unauthorized -- Your TOKEN is wrong.
//        const val ERROR_CODE_403 = 403 // Forbidden -- You're not allowed to do this request
//        const val ERROR_CODE_404 = 404 // Not Found -- The specified method could not be found.
//        const val ERROR_CODE_405 = 405 // Method Not Allowed -- You tried to access an API with an invalid method.
//        const val ERROR_CODE_406 = 406 // Not Acceptable -- You requested a format that isn't json.
        const val ERROR_CODE_409 = 409 // Conflict -- Account need verification.
//        const val ERROR_CODE_422 = 422 // Data not found.
//        const val ERROR_CODE_500 = 500 // Internal Server ApiResponseError -- We had a problem with our server. Try again later.
//        const val ERROR_CODE_503 = 503 // Service Unavailable -- We're temporarily offline for maintenance. Please try again later.
    }
}