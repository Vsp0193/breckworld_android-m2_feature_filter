package com.breckworld.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMActivity
import com.breckworld.databinding.ActivitySplashBinding
import com.breckworld.livedata.EventObserver
import com.breckworld.activity.HomeActivity
import com.breckworld.activity.IntroActivity
import com.breckworld.activity.LoginActivity

class SplashActivity : BaseMVVMActivity<SplashViewModel, ActivitySplashBinding, SplashActivity.Events>() {

    override fun viewModelClass() = SplashViewModel::class.java

    override fun layoutResId() = R.layout.activity_splash

    override fun navContainerId(): Int? = null

    override fun subscribeToEvents() {
        if (App.mLocalStore?.loginState!!) {
            //startActivity(Intent(this, IntroActivity::class.java))
            HomeActivity.start(this)
            finish()
        } else {
            LoginActivity.start(this)
            finish()
        }
        /*viewModel.eventsLiveData.observe(this, EventObserver {
            when (it) {
                Events.OPEN_MAIN -> {
                    //MainActivity.start(this)
                    HomeActivity.start(this)
                    finish()
                }
                Events.OPEN_LOGIN -> {
                    LoginActivity.start(this)
                    //HomeActivity.start(this)
                    finish()
                }
            }
        })*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.moveNext()
    }

    enum class Events {
        OPEN_MAIN,
        OPEN_LOGIN
    }
}
