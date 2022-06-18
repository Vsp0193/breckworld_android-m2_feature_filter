package com.breckworld.util

import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.breckworld.R

class ProgressDialogFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var loader_text = getString(R.string.loading)
        if (arguments != null) {
            val bundle = arguments
            loader_text = bundle!!.getString(KEY_LOADER_TEXT)!!
        }
        val dialog = ProgressDialog(activity, theme)
        dialog.setMessage(loader_text)
        dialog.isIndeterminate = true
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        val drawable = ProgressBar(activity).indeterminateDrawable.mutate()
        drawable.setColorFilter(
            ContextCompat.getColor(requireActivity(), R.color.colorAccent),
            PorterDuff.Mode.SRC_IN
        )
        dialog.setIndeterminateDrawable(drawable)
        return dialog
    }

    companion object {
        @JvmField
        var KEY_LOADER_TEXT = "key_loader_text"
    }
}