package com.breckworld.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.breckworld.R
import com.breckworld.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    lateinit var binding : ActivityWebViewBinding
    var url: String = ""
    var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null) {
            title = intent.getStringExtra("title").toString()
            url = intent.getStringExtra("url").toString()
        }


        binding.imageViewBack.setOnClickListener(View.OnClickListener {
            super.onBackPressed()
        })

        binding.tvTitle.text = title.toString()

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.domStorageEnabled = true

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }

        binding.webView.loadUrl(url)
    }
}