package com.breckworld.util

import android.app.Activity
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object AppUtil {

    private var isLoadingVisible = false
    private var progressDialog: ProgressDialogFragment? = null
    private const val SECOND_MILLIS : Long = 1000
    private const val MINUTE_MILLIS : Long = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS : Long = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS : Long = 24 * HOUR_MILLIS
    private const val WEEK_MILLIS : Long = 7 * DAY_MILLIS
    private const val MONTH_MILLIS : Long = 4 * WEEK_MILLIS

    fun hideSoftKeyboard(context: Context) {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            val view = (context as Activity).currentFocus
            if (view != null) {
                inputManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkConnected(@NonNull context: Context): Boolean {
        //1
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        //2
        val activeNetwork = connectivityManager.activeNetwork
        //3
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        //4
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun hideSoftKeyboard(context: Context, view: View) {
        val activity = context as Activity
        try {
            if (activity.currentFocus != null) {
                val inputMethodManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    fun base64ToString(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        val encoded: String =
            android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
        return encoded
    }

    fun showProgress(mContext: Context) {
        if (isLoadingVisible) {
            hideProgressWithCheck()
        }
        progressDialog = ProgressDialogFragment()
        val fragmentTransaction =
            (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
        val prev = mContext.supportFragmentManager.findFragmentByTag("progressDialog")
        if (prev != null) {
            fragmentTransaction.remove(prev)
        }
        fragmentTransaction.addToBackStack(null)
        try {
            progressDialog?.show(fragmentTransaction, "progressDialog")
            isLoadingVisible = true
        } catch(e: Exception) { e.printStackTrace() }
    }

    fun showProgress(mContext: Context, loader_text: String?) {
        if (isLoadingVisible) {
            hideProgress()
        }
        progressDialog = ProgressDialogFragment()
        val bundle = Bundle()
        bundle.putString(ProgressDialogFragment.KEY_LOADER_TEXT, loader_text)
        progressDialog!!.arguments = bundle
        val fragmentTransaction =
            (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
        val prev = mContext.supportFragmentManager.findFragmentByTag("progressDialog")
        if (prev != null) {
            fragmentTransaction.remove(prev)
        }
        fragmentTransaction.addToBackStack(null)
        progressDialog!!.show(fragmentTransaction, "progressDialog")
        isLoadingVisible = true
    }

    fun hideProgress() {
        try {
            if (progressDialog != null) {
                val fragmentTransaction = progressDialog!!.requireFragmentManager()
                    .beginTransaction()
                val prev = progressDialog!!.requireFragmentManager()
                    .findFragmentByTag("progressDialog")
                if (prev != null) {
                    fragmentTransaction.remove(prev)
                }
                fragmentTransaction.commitAllowingStateLoss()
                progressDialog!!.dismiss()
                isLoadingVisible = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideProgressWithCheck() {
        try {
            if (progressDialog != null && progressDialog!!.requireFragmentManager() != null) {
                val fragmentTransaction = progressDialog!!.requireFragmentManager()
                    .beginTransaction()
                val prev = progressDialog!!.requireFragmentManager()
                    .findFragmentByTag("progressDialog")
                if (prev != null) {
                    fragmentTransaction.remove(prev)
                }
                fragmentTransaction.commitAllowingStateLoss()
                progressDialog!!.dismiss()
                isLoadingVisible = false
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            progressDialog!!.dismiss()
            isLoadingVisible = false
        }
    }

    fun closeKeyboard(context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun closeKeyboard(mView: View) {
        val imm = mView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mView.windowToken, 0)
    }

    fun showSoftKeyboard(mActivity: Activity) {
        //Log.v("::::::::::::::;", "KLLKK")
        val imm = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun showMessage(view: View, message: String?, messageType: MessageType?) {
        val snackbar = Snackbar.make(view, message!!, Snackbar.LENGTH_LONG)
        val sbView = snackbar.view
        val params = sbView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        sbView.layoutParams = params
        snackbar.setActionTextColor(Color.WHITE)
        when (messageType) {
            MessageType.INFO -> sbView.setBackgroundColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.colorAccent
                )
            )
            MessageType.ERROR -> sbView.setBackgroundColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.colorAccent
                )
            )
            MessageType.NOTIFY -> sbView.setBackgroundColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.colorAccent
                )
            )
            else -> sbView.setBackgroundColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.colorAccent
                )
            )
        }
        snackbar.show()
    }

    enum class MessageType {
        INFO, ERROR, NOTIFY, DEFAULT
    }

    fun convertDpToPixel(dp: Int, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun changeDateFormat(date: String?, format: String?): String? { // 2021-12-15T04:57:57.296Z
        var finalDate: String? = null
        try {
            //current date format
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val objDate = dateFormat.parse(date)

            //Expected date format
            val dateFormat2 = SimpleDateFormat(format)
            val convertDate = dateFormat2.format(objDate)
            finalDate = convertDate

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return finalDate
    }

    fun convertStringToDate(dateFormat: String) : Date{
        var date: Date? = null
        val format = SimpleDateFormat("yyyy-MM-dd")
        try {
            date = format.parse(dateFormat)
            System.out.println(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date!!
    }

    fun convertStringToLong(date: String) : Long{
        var startDate: Long? = null
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date = sdf.parse(date)
            startDate = date.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return startDate!!
    }

    fun getTimeAgo(time: Long): String? {
        var time = time
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }

        // TODO: localize
        val diff = now - time
        return if (diff < MINUTE_MILLIS || diff < 2 * MINUTE_MILLIS || diff < 50 * MINUTE_MILLIS ||
            diff < 90 * MINUTE_MILLIS || diff < 24 * HOUR_MILLIS || diff < 48 * HOUR_MILLIS) {
            "today"
        } else if (diff < 7 * DAY_MILLIS) {
            ""+ diff / DAY_MILLIS + " days ago"
        } else if (diff < 2 * WEEK_MILLIS) {
            "a week ago"
        } else if (diff < 4 * WEEK_MILLIS) {
            ""+ diff / WEEK_MILLIS + " weeks ago"
        } else if (diff < 2 * MONTH_MILLIS) {
            "a month ago"
        } else {
            ""+ diff / MONTH_MILLIS + " months ago"
        }
    }

    fun convertIntoDateTime(date: String?): String? { // 2021-12-15T04:57:57.296Z
        var finalDate: String? = null
        try {
            //given date format
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.getDefault())
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            val objDate = dateFormat.parse(date)

            // current date format
            val curDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val convertCurrentDate = curDateFormat.format(objDate)

            //Expected time format
            val timeFormat = SimpleDateFormat("hh:mm a")
            val convertTime = timeFormat.format(objDate)
            finalDate = convertTime.replace("am", "Am").replace("pm", "Pm")

            //Expected date format
            val dateFormat2 = SimpleDateFormat("dd MMM")
            val convertDate = dateFormat2.format(objDate)


            if (convertCurrentDate.equals(currentDate())) {
                return finalDate
            } else if (convertCurrentDate.equals(yesterdayDate())) {
                return "Yesterday"
            } else {
                return convertDate
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return finalDate
    }

    fun currentDate(): String {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return df.format(c)
    }

    fun yesterdayDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        return dateFormat.format(cal.time)
    }

    /**
     * get last visible position of recycler view
     * @param rv
     * @return
     */
    open fun getLastVisiblePosition(rv: RecyclerView?): Int {
        if (rv != null) {
            val layoutManager = rv
                .layoutManager
            if (layoutManager is LinearLayoutManager) {
                return layoutManager
                    .findLastVisibleItemPosition()
            }
        }
        return 0
    }

    /**
     * Conversion long date time to String
     * @param time
     */
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm")
        return format.format(date)
    }

    /**
     * Get display metrics
     * @param context
     */
    fun getDeviceMetrics(context: Context): DisplayMetrics? {
        val metrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        display.getMetrics(metrics)
        return metrics
    }

    /**
     * Make Statusbar transparent
     * @param a
     */
    fun makeStatusBarTransparent(a: Int, activity: Activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

                } else {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }

                if (a == 1) {
                    val decorView: View = activity.getWindow().getDecorView()
                    var systemUiVisibilityFlags = decorView.systemUiVisibility
                    systemUiVisibilityFlags =
                        systemUiVisibilityFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                    statusBarColor = Color.TRANSPARENT
                    decorView.setSystemUiVisibility(systemUiVisibilityFlags)

                } else {

                    val decorView: View = activity.getWindow().getDecorView()
                    var systemUiVisibilityFlags = decorView.systemUiVisibility
                    systemUiVisibilityFlags =
                        systemUiVisibilityFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                    statusBarColor = Color.WHITE
                    decorView.setSystemUiVisibility(systemUiVisibilityFlags)
                }
                // statusBarColor =
                //statusBarColor = Color.TRANSPARENT
            }
        }
    }

    fun findImageResource(context: Context, imageName: String): Drawable {
        val uri = "@drawable/${imageName.lowercase()}"
        val imageResource: Int =
            context.resources.getIdentifier(uri, null, context.getPackageName())
        return ContextCompat.getDrawable(context, imageResource)!!
    }

    fun isValidEmail(target: String?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun byteArrayImage(imageBitmap: Bitmap) : ByteArray{
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun stringToBase64(imagePath: String): ByteArray {
        val bm = BitmapFactory.decodeFile(imagePath)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
        return baos.toByteArray()
    }

    fun getVisibleText(tv: TextView): String? {
        val lastLine =
            if (tv.maxLines < 1 || tv.maxLines > tv.lineCount) tv.lineCount else tv.maxLines
        return if (tv.ellipsize != null && tv.ellipsize == TextUtils.TruncateAt.END) {
            val ellCount = tv.layout.getEllipsisCount(lastLine - 1)
            if (ellCount > 0 && tv.length() > ellCount) tv.text.toString()
                .substring(0, tv.getText().toString().length - ellCount) else tv.text.toString()
        } else {
            val end = tv.layout.getLineEnd(lastLine - 1)
            tv.text.toString().substring(0, end)
        }
    }

    fun removeChars(str: String, chars: Int): String? {
        return str.substring(0, str.length - chars)
    }

    fun convertIntArrayToCommaSeparated(array: ArrayList<Int>): String {
        var filter = ""
        for (i in array.indices) {
            if (i == array.size - 1) {
                filter = filter + array.get(i).toString()
            } else {
                filter = filter + array.get(i).toString() + ","
            }
        }
        return filter
    }

}