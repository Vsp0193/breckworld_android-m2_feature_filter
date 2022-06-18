package com.breckworld.utils

import android.content.Context
import android.content.SharedPreferences
import com.breckworld.model.login.LoginModel
import com.breckworld.model.userprofile.Profile

import com.google.gson.Gson

class AppLocalStore private constructor(context: Context) {
    private val mPreferences: SharedPreferences

    fun saveAccessToken(device_token: String?) {
        mPreferences.edit().putString(KEY_ACCESS_TOKEN, device_token).apply()
    }

    val accessToken: String?
        get() = mPreferences.getString(KEY_ACCESS_TOKEN, Constant.BLANK_STRING)

    fun saveToolTipSeen(login: Boolean?) {
        mPreferences.edit().putBoolean(KEY_TOOLTIP_SEEN, login!!).apply()
    }

    val isToolTipSeen: Boolean
        get() = mPreferences.getBoolean(KEY_TOOLTIP_SEEN, false)

    fun saveLoginState(login: Boolean?) {
        mPreferences.edit().putBoolean(KEY_LOGIN, login!!).apply()
    }

    val loginState: Boolean
        get() = mPreferences.getBoolean(KEY_LOGIN, false)

    fun clearDataOnLogout() {
        //clear
        mPreferences.edit().putBoolean(KEY_LOGIN, false).apply()
        mPreferences.edit().putString(KEY_LOGIN_DATA, Constant.BLANK_STRING).apply()
        mPreferences.edit().putString(KEY_USER_PROFILE, Constant.BLANK_STRING).apply()
        mPreferences.edit().putString(KEY_ACCESS_TOKEN, Constant.BLANK_STRING).apply()
        mPreferences.edit().putBoolean(KEY_TOOLTIP_SEEN, false).apply()
    }

    fun setLoginDetail(loginData: LoginModel?) {
        val prefsEditor = mPreferences.edit()
        val gson = Gson()
        prefsEditor.putString(KEY_LOGIN_DATA, gson.toJson(loginData))
        prefsEditor.commit()
    }

    fun getLoginDetail(): LoginModel? {
        val userDetailsString = mPreferences.getString(KEY_LOGIN_DATA, null)
        return if (userDetailsString != null && !userDetailsString.isEmpty()) {
            var userDetail: LoginModel
            val gson = Gson()
            val userJson = gson.fromJson(
                userDetailsString,
                LoginModel::class.java
            )
            userDetail = userJson as LoginModel
            return userDetail

        } else {
            null
        }
    }

    fun setUserProfile(profileData: Profile?) {
        val prefsEditor = mPreferences.edit()
        val gson = Gson()
        prefsEditor.putString(KEY_USER_PROFILE, gson.toJson(profileData))
        prefsEditor.commit()
    }

    fun getUserProfile(): Profile? {
        val userDetailsString = mPreferences.getString(KEY_USER_PROFILE, null)
        return if (userDetailsString != null && !userDetailsString.isEmpty()) {
            var userDetail: Profile
            val gson = Gson()
            val userJson = gson.fromJson(
                userDetailsString,
                Profile::class.java
            )
            userDetail = userJson as Profile
            return userDetail

        } else {
            null
        }
    }

    fun logOut() {
        val editor = mPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private var sLocalStore: AppLocalStore? = null
        private const val PREF_NAME = "app_local_prefs"
        private const val KEY_LOGIN = "key_app_login"
        private const val KEY_TOOLTIP_SEEN = "key_tooltip_seen"
        private const val KEY_LOGIN_DATA = "key_login_data"
        private const val KEY_USER_PROFILE = "key_user_profile"
        private const val KEY_ACCESS_TOKEN = "key_access_token"

        /**
         * Get Singleton instance of ApiLocal store create new if needed
         *
         * @param context Context
         * @return singleton instance
         */
        @JvmStatic
        fun getInstance(context: Context): AppLocalStore? {
            if (sLocalStore == null) {
                sLocalStore = AppLocalStore(context)
            }
            return sLocalStore
        }
    }

    init {
        mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}