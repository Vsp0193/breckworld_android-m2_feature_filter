package com.breckworld.repository.settings

import android.content.Context
import android.content.SharedPreferences
import com.breckworld.App

object SharedPreferencesSettingsRepository : SettingsRepository {

    private const val SETTINGS_FILE = "settingsFile"
    private const val KEY_TOKEN = "KEY_TOKEN"
    private const val KEY_EXPIRES_TIME = "KEY_EXPIRES_TIME"
    private const val KEY_SHOW_PROFILE = "KEY_SHOW_PROFILE"
    private const val KEY_IS_COMBINED = "KEY_IS_COMBINED"
    private const val USER_PROFILE = "USER_PROFILE"
    private const val KEY_GUIDE_HOME = "KEY_GUIDE_HOME"
    private const val KEY_GUIDE_SEARCH = "KEY_GUIDE_SEARCH"
    private const val KEY_GUIDE_AR_VIEW = "KEY_GUIDE_AR_VIEW"
    private const val KEY_GUIDE_STARS = "KEY_GUIDE_STARS"
    private const val KEY_GUIDE_WALLET = "KEY_GUIDE_WALLET"
    private const val KEY_IS_FIRST_RUN = "KEY_IS_FIRST_RUN"

    private fun getSharedPreferences(): SharedPreferences {
        val applicationContext = App.applicationContext()
        return applicationContext.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE)
    }

    override fun getToken(): String {
        return getSharedPreferences().getString(KEY_TOKEN, "")!!
    }

    override fun getAccessToken(): String {
        return if (getToken().isBlank()) {
            ""
        } else {
            getToken().split(" ")[1]
        }
    }

    override fun saveToken(token: String) {
        with(getSharedPreferences().edit()) {
            putString(KEY_TOKEN, token)
            apply()
        }
    }

    override fun removeToken() {
        with(getSharedPreferences().edit()) {
            putString(KEY_TOKEN, "")
            apply()
        }
    }

    override fun getExpiresTime(): Long {
        return getSharedPreferences().getLong(KEY_EXPIRES_TIME, -1)
    }

    override fun saveExpiresTime(expiresTime: Long) {
        with(getSharedPreferences().edit()) {
            putLong(KEY_EXPIRES_TIME, expiresTime)
            apply()
        }
    }

    override fun removeExpiresTime() {
        with(getSharedPreferences().edit()) {
            remove(KEY_EXPIRES_TIME)
            apply()
        }
    }

    override fun clear() {
        with(getSharedPreferences().edit()) {
            remove(KEY_EXPIRES_TIME)
            remove(KEY_TOKEN)
            remove(KEY_GUIDE_HOME)
            remove(KEY_GUIDE_SEARCH)
            remove(KEY_GUIDE_AR_VIEW)
            remove(KEY_GUIDE_STARS)
            remove(KEY_GUIDE_WALLET)
            apply()
        }
    }

    override fun isGuideHome(): Boolean {
        return getSharedPreferences().getBoolean(KEY_GUIDE_HOME, false)
    }

    override fun isGuideSearch(): Boolean {
        return getSharedPreferences().getBoolean(KEY_GUIDE_SEARCH, false)
    }

    override fun isGuideArView(): Boolean {
        return getSharedPreferences().getBoolean(KEY_GUIDE_AR_VIEW, false)
    }

    override fun isGuideStars(): Boolean {
        return getSharedPreferences().getBoolean(KEY_GUIDE_STARS, false)
    }

    override fun isGuideWallet(): Boolean {
        return getSharedPreferences().getBoolean(KEY_GUIDE_WALLET, false)
    }

    override fun saveGuideHome(isGuideShow: Boolean) {
        with(getSharedPreferences().edit()) {
            putBoolean(KEY_GUIDE_HOME, isGuideShow)
            apply()
        }
    }

    override fun saveGuideSearch(isGuideShow: Boolean) {
        with(getSharedPreferences().edit()) {
            putBoolean(KEY_GUIDE_SEARCH, isGuideShow)
            apply()
        }
    }

    override fun saveGuideArView(isGuideShow: Boolean) {
        with(getSharedPreferences().edit()) {
            putBoolean(KEY_GUIDE_AR_VIEW, isGuideShow)
            apply()
        }
    }

    override fun saveGuideStars(isGuideShow: Boolean) {
        with(getSharedPreferences().edit()) {
            putBoolean(KEY_GUIDE_STARS, isGuideShow)
            apply()
        }
    }

    override fun saveGuideWallet(isGuideShow: Boolean) {
        with(getSharedPreferences().edit()) {
            putBoolean(KEY_GUIDE_WALLET, isGuideShow)
            apply()
        }
    }

    override fun startGuide() {
        with(getSharedPreferences().edit()) {
            putBoolean(KEY_GUIDE_HOME, true)
            putBoolean(KEY_GUIDE_SEARCH, true)
            putBoolean(KEY_GUIDE_AR_VIEW, true)
            putBoolean(KEY_GUIDE_STARS, true)
            putBoolean(KEY_GUIDE_WALLET, true)
            apply()
        }
    }

    override fun isFirstRun(): Boolean {
        return getSharedPreferences().getBoolean(KEY_IS_FIRST_RUN, true)
    }

    override fun saveFirstRun(isFirstRun: Boolean) {
        with(getSharedPreferences().edit()) {
            putBoolean(KEY_IS_FIRST_RUN, isFirstRun)
            apply()
        }
    }

    override var isCombined: Boolean
        get() = getSharedPreferences().getBoolean(KEY_IS_COMBINED, false)
        set(value) = with(getSharedPreferences().edit()) {
            putBoolean(KEY_IS_COMBINED, value)
            apply()
        }
}