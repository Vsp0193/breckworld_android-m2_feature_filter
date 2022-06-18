package com.breckworld.activity

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.breckworld.R
import com.breckworld.databinding.ActivityLogin2Binding
import com.breckworld.databinding.ActivityRegistrationBinding
import com.breckworld.repository.Repository2
import com.breckworld.util.AppUtil
import com.breckworld.viewmodel.LoginVM
import com.breckworld.viewmodel.SignupVM
import com.breckworld.viewmodel.factory.LoginVMFactory
import com.breckworld.viewmodel.factory.SignupVMFactory
import com.breckworld.webservice.ApiClient
import com.breckworld.webservice.ApiInterface

class RegistrationActivity : AppCompatActivity() {

    lateinit var registrationBinding: ActivityRegistrationBinding
    private lateinit var signupViewModel: SignupVM
    private var apiService: ApiInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(registrationBinding.root)
        apiService = ApiClient.createService(ApiInterface::class.java, this)
        val repositories = Repository2(apiService!!, this)

        signupViewModel = ViewModelProvider(
            this, SignupVMFactory(repositories)
        ).get(SignupVM::class.java)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.colorBlackType1)

        val uri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.bubbles_video)
        registrationBinding.videoView.setVideoURI(uri)
        registrationBinding.videoView.start()
        registrationBinding.videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp ->
            mp.isLooping = true
        })

        registrationBinding.textViewNext.setOnClickListener {
            if (registrationBinding.editTextFirstName.text.toString().trim().length <= 0) {
                Toast.makeText(
                    this@RegistrationActivity,
                    resources.getString(R.string.please_enter_your_first_name),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (registrationBinding.editTextLastName.text.toString().trim().length <= 0) {
                Toast.makeText(
                    this@RegistrationActivity,
                    resources.getString(R.string.please_enter_your_last_name),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (registrationBinding.editTextEmailAddress.text.toString()
                    .trim().length <= 0
            ) {
                Toast.makeText(
                    this@RegistrationActivity,
                    resources.getString(R.string.please_enter_your_email_address),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!AppUtil.isValidEmail(
                    registrationBinding.editTextEmailAddress.text.trim().toString().trim()
                )
            ) {
                Toast.makeText(
                    this@RegistrationActivity,
                    resources.getString(R.string.please_enter_valid_email_address),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                signupViewModel.signupApiCall(
                    this@RegistrationActivity, this@RegistrationActivity,
                    registrationBinding.editTextFirstName.text.toString().trim(),
                    registrationBinding.editTextLastName.text.toString().trim(),
                    registrationBinding.editTextEmailAddress.text.toString().trim()
                )
            }
        }

        registrationBinding.textViewLogin.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}