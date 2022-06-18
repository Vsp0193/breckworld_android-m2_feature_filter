package com.breckworld.binding

import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.breckworld.extensions.gone
import com.breckworld.extensions.visible
import com.breckworld.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

@BindingAdapter("app:loadImage")
fun loadImage(imageView: ImageView, url: String) {
    Glide.with(imageView).load(url).into(imageView)
}

@BindingAdapter("app:loadResImage")
fun loadResImage(imageView: ImageView, drawableRes: Int) {
    Glide.with(imageView).load(drawableRes).into(imageView)
}

@BindingAdapter("app:loadAvatar")
fun loadAvatarImage(imageView: ImageView, url: String?) {
    if (url.isNullOrBlank()) Glide.with(imageView).applyDefaultRequestOptions(RequestOptions().centerCrop().circleCrop()).load(
        url
    ).into(
        imageView
    )
    else Glide.with(imageView).applyDefaultRequestOptions(RequestOptions().centerCrop().circleCrop()).load(url).into(
        imageView
    )
}


@BindingAdapter("app:maxLength")
fun setMaxLength(editText: EditText, maxLength: Int) {
    editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
}

@BindingAdapter("app:loadDrawable")
fun loadImage(imageView: ImageView, drawable: Drawable) {
    Glide.with(imageView).load(drawable).into(imageView)
}

@BindingAdapter("app:selected")
fun setSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
    view.isEnabled = isSelected
}

@BindingAdapter("app:visibility")
fun setVisible(view: View, visible: Boolean) {
    if (visible) view.visible() else view.gone()
}

@BindingAdapter("app:src")
fun setImageRes(imageView: ImageView, resId: Int?) {
    resId?.let {
        imageView.setImageResource(resId)
    }
}

@BindingAdapter(value = ["imageUrl", "radius", "placeholderId"], requireAll = false)
fun loadImageWithCorners(imageView: ImageView, url: String?, radius: Int?, placeholderId: Int?) {
    if (!url.isNullOrBlank()) {
        if (radius == null) {
            Utils.setImageFromUrl(url, placeholderId, imageView)
        } else {
            Utils.setImageFromUrlWithCorners(url, radius, placeholderId, imageView)
        }
    } else {
        placeholderId?.let { Utils.setImageFromResources(it, imageView) }
    }
}

@BindingAdapter(value = ["circleImageResId"])
fun setCircleImageFromResources(imageView: ImageView, circleImageResId: Int?) {
    if (circleImageResId != null) {
        Utils.setCircleImageFromResources(circleImageResId, imageView)
    }
}

@BindingAdapter(value = ["circleImageFile"])
fun setCircleImageFromFile(imageView: ImageView, circleImageFile: File?) {
    if (circleImageFile != null) {
        Utils.setImageFromFile(circleImageFile, imageView)
    }
}
