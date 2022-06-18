package com.breckworld.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.VideoView
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMActivity
import com.breckworld.databinding.ActivityLoginBinding
import com.breckworld.extensions.navigateTo
import com.breckworld.livedata.EventObserver
import kotlinx.android.synthetic.main.activity_login.*


/**
 * @author Dmytro Bondarenko
 *         Date: 21.05.2019
 *         Time: 16:58
 *         E-mail: bondes87@gmail.com
 */
class LoginActivity : BaseMVVMActivity<LoginViewModel, ActivityLoginBinding, LoginActivity.Events>() {

    override fun viewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun layoutResId(): Int = R.layout.activity_login

    override fun navContainerId(): Int? = R.id.container

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController

        /*val view = findViewById(R.id.videoLayout) as VideoLayout
        val layoutRoot = findViewById(R.id.layoutRoot) as RelativeLayout
        val path = "android.resource://" + packageName + "/" + R.raw.bubbles_video

        view.setGravity(VideoLayout.VGravity.centerCrop)
        view.setIsLoop(true)
        view.setPathOrUrl(path)

        layoutRoot.addView(view)*/
    }

    override fun onBackPressed() {
        if (navController?.currentDestination?.id == R.id.tutorialFragment2) {
            navController?.navigateTo(R.id.mainActivity)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun showNoInternetConnection() {
        //showNavigationBarToast(R.string.no_inernet, background = R.drawable.bg_red_round_top_20dp)
    }

    override fun getStatusBarHeight() = cifl_container.statusBarHeight

    enum class Events {
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}