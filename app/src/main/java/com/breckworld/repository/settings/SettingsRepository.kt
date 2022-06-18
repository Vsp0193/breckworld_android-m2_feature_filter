package com.breckworld.repository.settings

interface SettingsRepository {

    fun getToken(): String

    fun saveToken(token: String)

    fun removeToken()

    fun getAccessToken(): String

    fun getExpiresTime(): Long

    fun saveExpiresTime(expiresTime: Long)

    fun removeExpiresTime()

    fun clear()

    fun isGuideHome(): Boolean

    fun isGuideSearch(): Boolean

    fun isGuideArView(): Boolean

    fun isGuideStars(): Boolean

    fun isGuideWallet(): Boolean

    fun saveGuideHome(isGuideShow: Boolean)

    fun saveGuideSearch(isGuideShow: Boolean)

    fun saveGuideArView(isGuideShow: Boolean)

    fun saveGuideStars(isGuideShow: Boolean)

    fun saveGuideWallet(isGuideShow: Boolean)

    fun startGuide()

    var isCombined: Boolean

    fun isFirstRun(): Boolean

    fun saveFirstRun(isFirstRun: Boolean)
}