package com.breckworld.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationSet
import androidx.navigation.ui.NavigationUI
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMActivity
import com.breckworld.databinding.ActivityMainBinding
import com.breckworld.livedata.EventObserver
import com.breckworld.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMVVMActivity<MainViewModel, ActivityMainBinding, MainActivity.Events>() {

    var toastAnimation: AnimationSet? = null

    override fun viewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun layoutResId(): Int = R.layout.activity_main

    override fun navContainerId(): Int? = R.id.container

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomBar()
    }

    override fun logout() {
        LoginActivity.start(this)
        finish()
    }

    override fun showNoInternetConnection() {
        //showNavigationBarToast(R.string.no_inernet, background = R.drawable.bg_red_round_top_20dp)
    }

    private fun setupBottomBar() {
        navController?.let {
            NavigationUI.setupWithNavController(bottom_navigation_view, it)
        }
    }

    fun getBottomBarItem(resId: Int): View {
        return bottom_navigation_view.findViewById(resId)
    }

    override fun getStatusBarHeight() = cifl_container.statusBarHeight

    override fun showBottomNavigation(show: Boolean) {
        //TODO: bottom navigation problem
        if (show) {
            bottom_navigation_view?.visibility = View.VISIBLE
        } else {
            bottom_navigation_view?.visibility = View.GONE
        }
    }

    enum class Events {
        TOKEN_REFRESHED,
        TOKEN_REFRESHED_FAILED,
        TOKEN_REFRESH_NETWORK_ERROR
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}