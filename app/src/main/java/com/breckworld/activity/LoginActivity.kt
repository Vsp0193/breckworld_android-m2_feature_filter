package com.breckworld.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.breckworld.R
import com.breckworld.databinding.ActivityLogin2Binding
import com.breckworld.repository.Repository2
import com.breckworld.util.AppUtil
import com.breckworld.viewmodel.LoginVM
import com.breckworld.viewmodel.factory.LoginVMFactory
import com.breckworld.webservice.ApiClient
import com.breckworld.webservice.ApiInterface


class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLogin2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        var scrollView : ScrollView
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        window.navigationBarColor = ContextCompat.getColor(this, R.color.colorBlackType1)

        val uri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.bubbles_video)
        loginBinding.videoView.setVideoURI(uri)
        loginBinding.videoView.start()
        loginBinding.videoView.setOnPreparedListener(OnPreparedListener { mp -> mp.isLooping = true })

        loginBinding.textViewNext.setOnClickListener {
            if (loginBinding.editTextEmailAddress.text.length != 0) {
                if (AppUtil.isValidEmail(loginBinding.editTextEmailAddress.text.trim().toString())) {
                    val intent = Intent(this@LoginActivity, PasscodeActivity::class.java)
                    intent.putExtra("email", loginBinding.editTextEmailAddress.text.toString().trim())
                    intent.putExtra("cameFrom", "Login")
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        resources.getString(R.string.please_enter_valid_email_address),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    resources.getString(R.string.please_enter_your_email_address),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        loginBinding.textViewRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}