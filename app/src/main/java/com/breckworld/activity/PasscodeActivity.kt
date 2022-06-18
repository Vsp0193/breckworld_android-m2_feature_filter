package com.breckworld.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.breckworld.App
import com.breckworld.R
import com.breckworld.app.repository.ResponseListener
import com.breckworld.databinding.ActivityPasscodeBinding
import com.breckworld.repository.Repository2
import com.breckworld.util.AppUtil
import com.breckworld.viewmodel.LoginVM
import com.breckworld.viewmodel.factory.LoginVMFactory
import com.breckworld.webservice.ApiClient
import com.breckworld.webservice.ApiInterface

class PasscodeActivity : AppCompatActivity() {

    lateinit var passcodeBinding: ActivityPasscodeBinding
    private lateinit var loginViewModel: LoginVM
    private var apiService: ApiInterface? = null
    var pin: String = ""
    var pintext: String = ""
    private var email: String = ""
    private var cameFrom: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        passcodeBinding = ActivityPasscodeBinding.inflate(layoutInflater)
        setContentView(passcodeBinding.root)
        apiService = ApiClient.createService(ApiInterface::class.java, this)
        val repositories = Repository2(apiService!!, this)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.colorBlackType1)

        passcodeBinding.layoutProgress.visibility = View.GONE

        if (intent != null) {
            email = intent.getStringExtra("email").toString().trim()
            cameFrom = intent.getStringExtra("cameFrom").toString()
        }

        if (cameFrom.equals("Login")) {
            passcodeBinding.textViewTitle.text = resources.getString(R.string.please_enter_the_passcode)
            passcodeBinding.textViewDes.text = resources.getString(R.string.enter_the_5_digits_verification_code_you_have)
            passcodeBinding.textViewForgotPassword.text = resources.getString(R.string.forgot_passcode)
        } else {
            passcodeBinding.textViewTitle.text = resources.getString(R.string.we_ve_sent_a_new_passcode)
            passcodeBinding.textViewDes.text = resources.getString(R.string.enter_the_5_digit_verification_code_you_just_received_via_email_please_check_junk_folder_too)
            passcodeBinding.textViewForgotPassword.text = resources.getString(R.string.resend_passcode)
        }

        loginViewModel = ViewModelProvider(
            this, LoginVMFactory(repositories)
        ).get(LoginVM::class.java)

        keyBoardClickListener()
        passcodeBinding.pinView.addTextChangedListener {
            if (it?.length == 5) {
                passcodeBinding.layoutProgress.visibility = View.VISIBLE
                loginViewModel.loginApiCall(
                    this@PasscodeActivity,
                    this@PasscodeActivity,
                    email,
                    pin
                )
            }
        }

        passcodeBinding.textViewForgotPassword.setOnClickListener {
            loginViewModel.forgotPasscodeApiCall(
                this@PasscodeActivity,
                this@PasscodeActivity,
                cameFrom,
                email
            )
        }

        loginViewModel.loginLiveData.observe(this, Observer {
            when (it) {
                is ResponseListener.Success -> {
                    //loginViewModel.loginLiveData.removeObservers(this)
                    passcodeBinding.layoutProgress.visibility = View.GONE
                    clearPinView()
                    if (it.data?.access_token != null) {
                        App.mLocalStore?.setLoginDetail(it.data)
                        App.mLocalStore?.saveLoginState(true)
                        App.mLocalStore?.saveAccessToken(it.data.access_token)
                        if (cameFrom.equals("Login")) {
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        } else {
                            val intent = Intent(this, IntroActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            it.data?.error_description.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is ResponseListener.Failure -> {
                    passcodeBinding.layoutProgress.visibility = View.GONE
                    loginViewModel.loginLiveData.removeObservers(this)
                    clearPinView()
                    Toast.makeText(this, it.errorMessage.toString(), Toast.LENGTH_SHORT).show()
                }
                is ResponseListener.Error -> {
                    passcodeBinding.layoutProgress.visibility = View.GONE
                    loginViewModel.loginLiveData.removeObservers(this)
                    clearPinView()
                    Toast.makeText(this, it.errorMessage.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun keyBoardClickListener() {
        passcodeBinding.layoutKeyboard.textOne.setOnClickListener {
            if (pin.length < 5) {
                pin = pin + "1"
                pintext = pintext + getString(R.string.dot)
            }
            passcodeBinding.pinView.setText(pintext)
        }

        passcodeBinding.layoutKeyboard.textTwo.setOnClickListener {
            if (pin.length < 5) {
                pin = pin + "2"
                pintext = pintext + getString(R.string.dot)
            }
            passcodeBinding.pinView.setText(pintext)
        }

        passcodeBinding.layoutKeyboard.textThree.setOnClickListener {
            if (pin.length < 5) {
                pin = pin + "3"
                pintext = pintext + getString(R.string.dot)
            }
            passcodeBinding.pinView.setText(pintext)
        }

        passcodeBinding.layoutKeyboard.textFour.setOnClickListener {
            if (pin.length < 5) {
                pin = pin + "4"
                pintext = pintext + getString(R.string.dot)
            }
            passcodeBinding.pinView.setText(pintext)
        }

        passcodeBinding.layoutKeyboard.textFive.setOnClickListener {
            if (pin.length < 5) {
                pin = pin + "5"
                pintext = pintext + getString(R.string.dot)
            }
            passcodeBinding.pinView.setText(pintext)
        }

        passcodeBinding.layoutKeyboard.textSix.setOnClickListener {
            if (pin.length < 5) {
                pin = pin + "6"
                pintext = pintext + getString(R.string.dot)
            }
            passcodeBinding.pinView.setText(pintext)
        }

        passcodeBinding.layoutKeyboard.textSeven.setOnClickListener {
            if (pin.length < 5) {
                pin = pin + "7"
                pintext = pintext + getString(R.string.dot)
            }
            passcodeBinding.pinView.setText(pintext)
        }

        passcodeBinding.layoutKeyboard.textEight.setOnClickListener {
            if (pin.length < 5) {
                pin = pin + "8"
                pintext = pintext + getString(R.string.dot)
            }
            passcodeBinding.pinView.setText(pintext)
        }

        passcodeBinding.layoutKeyboard.textNine.setOnClickListener {
            if (pin.length < 5) {
                pin = pin + "9"
                pintext = pintext + getString(R.string.dot)
            }
            passcodeBinding.pinView.setText(pintext)
        }

        passcodeBinding.layoutKeyboard.textZero.setOnClickListener {
            if (pin.length < 5) {
                pin = pin + "0"
                pintext = pintext + getString(R.string.dot)
            }
            passcodeBinding.pinView.setText(pintext)
        }

        passcodeBinding.layoutKeyboard.imgBack.setOnClickListener {
            pin = removeLastDigit(pin).toString()
            pintext = removeLastDigit(pintext).toString()
            passcodeBinding.pinView.setText(pintext)
        }
    }

    fun removeLastDigit(str: String?): String? {
        var str = str
        if (str != null && str.length > 0) {
            str = str.substring(0, str.length - 1)
        }
        return str
    }

    fun clearPinView() {
        pin = ""
        pintext = ""
        passcodeBinding.pinView.setText(pintext)
    }
}