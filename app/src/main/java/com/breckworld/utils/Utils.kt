package com.breckworld.utils

import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.regex.Pattern

object Utils {
    fun isEmailValid(email: String?): Boolean {
        val pattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        val matcher = pattern.matcher(email)

        return matcher.matches()
    }

    fun getMillisecondFromGreenwich() = Calendar.getInstance(TimeZone.getTimeZone("gmt")).timeInMillis

    fun setImageFromUrl(url: String, placeholderId: Int?, imageView: ImageView) {
        if (placeholderId == null) {
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(imageView)
        } else {
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions().transform(CenterCrop()).placeholder(placeholderId).error(placeholderId))
                .apply(RequestOptions().transform(CenterCrop()))
                .into(imageView)
        }
    }

    fun setImageFromUrlWithCorners(url: String, radius: Int?, placeholderId: Int?, imageView: ImageView) {
        val multiTransformation = MultiTransformation(
            CenterCrop(),
            RoundedCorners(radius!!)
        )
        if (placeholderId == null) {
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .apply(RequestOptions().transform(multiTransformation))
                .into(imageView)
        } else {
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .apply(RequestOptions().transform(multiTransformation))
                .apply(RequestOptions().transform(multiTransformation).placeholder(placeholderId).error(placeholderId))
                .into(imageView)
        }
    }

    fun setImageFromResources(resIdImage: Int, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(resIdImage)
            .apply(RequestOptions().transform(CenterCrop()))
            /*.apply(RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true))*/
            .into(imageView)
    }

    fun setCircleImageFromResources(resIdImage: Int, imageView: ImageView) {
        val multiTransformation = MultiTransformation(
            CenterCrop(),
            CircleCrop()
        )

        Glide.with(imageView.context)
            .load(resIdImage)
            .apply(bitmapTransform(multiTransformation))
            .into(imageView)
    }

    fun setImageFromFile(file: File, imageView: ImageView) {
        val multiTransformation = MultiTransformation(
            CenterCrop(),
            CircleCrop()
        )

        Glide.with(imageView.context)
            .load(file)
            .apply(bitmapTransform(multiTransformation))
            .into(imageView)
    }

    fun encodeToBase64(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.DEFAULT)
    }

    fun getBytesFromFile(file: File): ByteArray? {
        val fileBytes = ByteArray(file.length().toInt())
        try {
            FileInputStream(file).use { inputStream -> inputStream.read(fileBytes) }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return fileBytes
    }
}