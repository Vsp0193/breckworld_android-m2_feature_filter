package com.breckworld.repository.remote.http.error

import okhttp3.ResponseBody
import org.json.JSONObject

fun getErrorMessage(errorBody: ResponseBody?): String {
    var jObjError: String
    try {
        jObjError = JSONObject(errorBody?.string()!!).getString("message")
    } catch (e: Exception) {
        jObjError = errorBody?.string() ?: ""
    }
    return jObjError
}
