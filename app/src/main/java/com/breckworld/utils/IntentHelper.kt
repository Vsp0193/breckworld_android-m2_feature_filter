package com.breckworld.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object IntentHelper {

    fun openInBrowser(context: Context, link: String?) {
        if (link.isNullOrBlank() || link.isNullOrEmpty()) return
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(browserIntent)
    }

    fun sendToMail(context: Context, mail: String, subject: String? = null, body: String? = null) {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mail, null
            )
        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject ?: "")
        emailIntent.putExtra(Intent.EXTRA_TEXT, body ?: "")
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }
}