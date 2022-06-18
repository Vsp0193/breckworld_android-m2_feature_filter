package com.breckworld

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ArrayRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.breckworld.utils.AppLocalStore
import com.breckworld.utils.AppLocalStore.Companion.getInstance
import timber.log.Timber
import java.lang.reflect.InvocationTargetException

class App: Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        if (mLocalStore == null) {
            mLocalStore = getInstance(this)
        }
        Timber.plant(Timber.DebugTree())
        initStetho()
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            try {
                val stethoClazz = Class.forName("com.facebook.stetho.Stetho")
                val method = stethoClazz.getMethod("initializeWithDefaults", Context::class.java)
                method.invoke(null, this)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }

        }
    }

    companion object {
        private var instance: App? = null
        var mLocalStore: AppLocalStore? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun getStringFromRes(@StringRes stringId: Int): String {
            return instance!!.getString(stringId)
        }

        fun getStringFromRes(@StringRes stringId: Int, vararg formatArgs: Any): String {
            return instance!!.getString(stringId, *formatArgs)
        }

        fun getStringArrayFromRes(@ArrayRes arrayId: Int): Array<String> {
            return instance!!.resources.getStringArray(arrayId)
        }

        fun getQuantityString(@PluralsRes stringId: Int, quantity: Int, vararg formatObjects: Any): String {
            return instance!!.resources.getQuantityString(stringId, quantity, *formatObjects)
        }

        fun getResColor(resId: Int): Int {
            return if (null == instance) {
                -1
            } else ContextCompat.getColor(instance!!, resId)
        }

        fun getResDimension(resId: Int): Float {
            return if (null == instance) {
                -1f
            } else instance?.resources!!.getDimension(resId)
        }

        fun getResDimensionPixelSize(resId: Int): Int {
            return if (null == instance) {
                -1
            } else instance?.resources!!.getDimensionPixelSize(resId)
        }

        fun convertSpToPixels(sp: Float): Float =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, instance?.resources!!.displayMetrics)

        fun getResFont(resId: Int): Typeface? {
            return if (null == instance) {
                null
            } else ResourcesCompat.getFont(instance!!, resId)
        }

        fun getResDrawable(resId: Int): Drawable? {
            return if (null == instance) {
                null
            } else ContextCompat.getDrawable(instance!!, resId)
        }
    }

}