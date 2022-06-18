package com.breckworld.view.omnivirt;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;
import java.text.ParseException;

class CustomWebViewClient extends WebViewClient {
    private final WeakReference<Activity> mActivityRef;

    public CustomWebViewClient(Activity activity) {
        this.mActivityRef = new WeakReference(activity);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Uri uri = Uri.parse(url);
        return this.handleUri(uri);
    }

    @RequiresApi(24)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri uri = request.getUrl();
        return this.handleUri(uri);
    }

    private boolean handleUri(Uri uri) {
        Log.i("CustomWebViewClient", "URI = " + uri);
        String host = uri.getHost();
        String scheme = uri.getScheme();
        Activity activity = (Activity)this.mActivityRef.get();
        if (uri.toString().indexOf("mailto:") == 0) {
            MailTo mt = null;

            try {
                mt = MailTo.parse(uri.toString());
                Intent i = this.newEmailIntent(activity, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
                Log.i("CustomWebViewClient", "To = " + mt.getTo());
                Log.i("CustomWebViewClient", "Subject = " + mt.getSubject());
                Log.i("CustomWebViewClient", "Body = " + mt.getBody());
                Log.i("CustomWebViewClient", "CC = " + mt.getCc());
                activity.startActivity(i);
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.EMAIL", new String[]{address});
        intent.putExtra("android.intent.extra.TEXT", body);
        intent.putExtra("android.intent.extra.SUBJECT", subject);
        intent.putExtra("android.intent.extra.CC", cc);
        intent.setType("message/rfc822");
        return intent;
    }
}