package com.breckworld.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.breckworld.App
import com.breckworld.R
import com.breckworld.adapter.NavigationAdapter
import com.breckworld.app.repository.ResponseListener
import com.breckworld.databinding.ActivityMain2Binding
import com.breckworld.extensions.Constants
import com.breckworld.fragment.*
import com.breckworld.model.userprofile.Link
import com.breckworld.repository.Repository2
import com.breckworld.viewmodel.HomeVM
import com.breckworld.viewmodel.factory.HomeVMFactory
import com.breckworld.webservice.ApiClient
import com.breckworld.webservice.ApiInterface
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.nav_drawer_menu.view.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType


class HomeActivity : AppCompatActivity() {

    lateinit var homeBinding: ActivityMain2Binding
    private lateinit var homeViewModel: HomeVM
    private var apiService: ApiInterface? = null
    var guideView: GuideView? = null
    private var linkItem: List<Link>? = null
    private var cameFrom = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.header_color)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.header_color)
        //hideSystemUI()

        apiService = ApiClient.createService(ApiInterface::class.java, this)
        val repositories = Repository2(apiService!!, this)
        homeViewModel = ViewModelProvider(
            this, HomeVMFactory(repositories)
        ).get(HomeVM::class.java)

        homeBinding.drawar.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if (intent != null) {
            cameFrom = intent.getStringExtra("cameFrom").toString()

            if (cameFrom.equals("Home")) {
                addFragmentToActivity(HomeFragment1())
                bottomNavItemSelected(true, false, false, false, false)
            } else if (cameFrom.equals("GeoStore")) {
                addFragmentToActivity(GeoStoreFragment())
                bottomNavItemSelected(false, true, false, false, false)
            } else if (cameFrom.equals("Fav")) {
                addFragmentToActivity(FavFragment())
                bottomNavItemSelected(false, false, true, false, false)
            } else if (cameFrom.equals("History")) {
                addFragmentToActivity(HistoryFragment())
                bottomNavItemSelected(false, false, false, true, false)
            } else if (cameFrom.equals("Wallet")) {
                addFragmentToActivity(WalletFragment())
                bottomNavItemSelected(false, false, false, false, true)
            } else {
                addFragmentToActivity(HomeFragment1())
                bottomNavItemSelected(true, false, false, false, false)
            }
        } else {
            addFragmentToActivity(HomeFragment1())
            //addFragmentToActivity(GeoStoreFragment())
        }

        homeViewModel.getUserProfileApi(App.mLocalStore?.accessToken)
        homeViewModel.userProfileLiveData.observe(this, Observer {
            when (it) {
                is ResponseListener.Success -> {
                    if (it.data?.profile != null) {
                        App.mLocalStore?.setUserProfile(it.data.profile)

                        // Add the links data for side navigation
                        var linkItem1 = it.data.links

                        var linkItem2 = listOf(
                            Link("Terms of Service", "", true),
                            Link("Privacy Policy", "", false),
                            Link("Start App Tutorial", "", false),
                            Link("Report an Issue", "", false),
                            Link("Logout", "", false)
                        )

                        linkItem = linkItem1 + linkItem2
                        setNavigationItem()

                        homeBinding.navigationView.textViewUserName.text =
                            it.data.profile.first_name + " " + it.data.profile.last_name
                        Glide.with(this).load(it.data.profile.profile_pic)
                            .apply(
                                RequestOptions.placeholderOf(R.drawable.user_placeholder)
                                    .dontAnimate()
                            )
                            .into(homeBinding.navigationView.imageViewUserProfile)
                        Glide.with(this).load(it.data.profile.profile_pic)
                            .apply(
                                RequestOptions.placeholderOf(R.drawable.user_placeholder)
                                    .dontAnimate()
                            )
                            .into(homeBinding.layoutDashboard.layoutHeader.imageViewProfile)

                        // Wallet badge
                        if (it.data.button_badges.wallet) {
                            homeBinding.layoutDashboard.bottomNavigationView.imageViewWalletBadge.visibility =
                                View.VISIBLE
                        } else {
                            homeBinding.layoutDashboard.bottomNavigationView.imageViewWalletBadge.visibility =
                                View.GONE
                        }

                        // History badge
                        if (it.data.button_badges.history) {
                            homeBinding.layoutDashboard.bottomNavigationView.imageViewHistoryBadge.visibility =
                                View.VISIBLE
                        } else {
                            homeBinding.layoutDashboard.bottomNavigationView.imageViewHistoryBadge.visibility =
                                View.GONE
                        }

                        // Favorite badge
                        if (it.data.button_badges.favorites) {
                            homeBinding.layoutDashboard.bottomNavigationView.imageViewFavBadge.visibility =
                                View.VISIBLE
                        } else {
                            homeBinding.layoutDashboard.bottomNavigationView.imageViewFavBadge.visibility =
                                View.GONE
                        }

                    } else {
                        Toast.makeText(
                            this@HomeActivity,
                            it.data?.error_description.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is ResponseListener.Failure -> {
                    //Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    App.mLocalStore?.clearDataOnLogout()
                    val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                is ResponseListener.Error -> {
                    //Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    App.mLocalStore?.clearDataOnLogout()
                    val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        })

        homeBinding.layoutDashboard.bottomNavigationView.layoutHome.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.container)

            // For hide the search functionality
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.setText("")
            homeBinding.layoutDashboard.layoutHeader.imageViewBack.visibility = View.GONE
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.clearFocus()
            if (fragment is HomeFragment1) {
                fragment.backClicked()
            }

            if (fragment is HomeFragment1 == false)
                setFragment(HomeFragment1())
            bottomNavItemSelected(true, false, false, false, false)
        }

        homeBinding.layoutDashboard.bottomNavigationView.layoutGeoStore.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.container)

            // For hide the search functionality
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.setText("")
            homeBinding.layoutDashboard.layoutHeader.imageViewBack.visibility = View.GONE
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.clearFocus()
            if (fragment is GeoStoreFragment) {
                fragment.backClicked()
            }

            if (fragment is GeoStoreFragment == false)
                setFragment(GeoStoreFragment())
            bottomNavItemSelected(false, true, false, false, false)
        }

        homeBinding.layoutDashboard.bottomNavigationView.layoutFavourites.setOnClickListener {
            homeBinding.layoutDashboard.bottomNavigationView.imageViewFavBadge.visibility =
                View.GONE
            val fragment = supportFragmentManager.findFragmentById(R.id.container)

            // For hide the search functionality
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.setText("")
            homeBinding.layoutDashboard.layoutHeader.imageViewBack.visibility = View.GONE
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.clearFocus()
            if (fragment is FavFragment) {
                fragment.backClicked()
            }

            if (fragment is FavFragment == false)
                setFragment(FavFragment())
            bottomNavItemSelected(false, false, true, false, false)
        }

        homeBinding.layoutDashboard.bottomNavigationView.layoutHistory.setOnClickListener {
            homeBinding.layoutDashboard.bottomNavigationView.imageViewHistoryBadge.visibility =
                View.GONE
            val fragment = supportFragmentManager.findFragmentById(R.id.container)

            // For hide the search functionality
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.setText("")
            homeBinding.layoutDashboard.layoutHeader.imageViewBack.visibility = View.GONE
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.clearFocus()
            if (fragment is HistoryFragment) {
                fragment.backClicked()
            }

            if (fragment is HistoryFragment == false)
                setFragment(HistoryFragment())
            bottomNavItemSelected(false, false, false, true, false)
        }

        homeBinding.layoutDashboard.bottomNavigationView.layoutWallet.setOnClickListener {
            homeBinding.layoutDashboard.bottomNavigationView.imageViewWalletBadge.visibility =
                View.GONE
            val fragment = supportFragmentManager.findFragmentById(R.id.container)

            // For hide the search functionality
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.setText("")
            homeBinding.layoutDashboard.layoutHeader.imageViewBack.visibility = View.GONE
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.clearFocus()
            if (fragment is WalletFragment) {
                fragment.backClicked()
            }

            if (fragment is WalletFragment == false)
                setFragment(WalletFragment())
            bottomNavItemSelected(false, false, false, false, true)
        }

        homeBinding.layoutDashboard.layoutHeader.imageViewProfile.setOnClickListener {
            if(homeBinding.layoutDashboard.layoutHeader.imageViewBack.visibility != View.VISIBLE) {
                homeBinding.drawar.openDrawer(Gravity.LEFT)
            }
        }

        homeBinding.layoutDashboard.layoutHeader.imageViewBack.setOnClickListener {
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.setText("")
            homeBinding.layoutDashboard.layoutHeader.imageViewBack.visibility = View.GONE
            val fragment = supportFragmentManager.findFragmentById(R.id.container)
            if (fragment is HomeFragment1) {
                fragment.backClicked()
            } else if (fragment is GeoStoreFragment) {
                fragment.backClicked()
            } else if (fragment is FavFragment) {
                fragment.backClicked()
            } else if (fragment is HistoryFragment) {
                fragment.backClicked()
            } else if (fragment is WalletFragment) {
                fragment.backClicked()
            }
        }

        homeBinding.layoutDashboard.layoutHeader.imageViewStackSearch.setOnClickListener {
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.setText("")
            //homeBinding.layoutDashboard.layoutHeader.imageViewBack.visibility = View.VISIBLE
            val fragment = supportFragmentManager.findFragmentById(R.id.container)
            if (fragment is HomeFragment1) {
                fragment.stackClicked()
            } else if (fragment is GeoStoreFragment) {
                fragment.stackClicked()
            } else if (fragment is FavFragment) {
                fragment.stackClicked()
            } else if (fragment is HistoryFragment) {
                fragment.stackClicked()
            } else if (fragment is WalletFragment) {
                fragment.stackClicked()
            }
        }

        homeBinding.layoutDashboard.layoutHeader.imageViewRWS.setOnClickListener {
            homeBinding.layoutDashboard.layoutHeader.editTextSearch.setText("")
            homeBinding.layoutDashboard.layoutHeader.imageViewBack.visibility = View.VISIBLE
            val fragment = supportFragmentManager.findFragmentById(R.id.container)
            if (fragment is HomeFragment1) {
                fragment.rwsClicked()
            } else if (fragment is GeoStoreFragment) {
                fragment.rwsClicked()
            } else if (fragment is FavFragment) {
                fragment.rwsClicked()
            } else if (fragment is HistoryFragment) {
                fragment.rwsClicked()
            } else if (fragment is WalletFragment) {
                fragment.rwsClicked()
            }
        }

        homeBinding.layoutDashboard.layoutHeader.editTextSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if(v.text?.length!! >= 3) {
                    homeBinding.layoutDashboard.layoutHeader.imageViewBack.visibility = View.VISIBLE
                    val fragment = supportFragmentManager.findFragmentById(R.id.container)
                    if (fragment is HomeFragment1) {
                        fragment.getSearch(v.text.toString())
                    } else if (fragment is GeoStoreFragment) {
                        fragment.getSearch(v.text.toString())
                    } else if (fragment is FavFragment) {
                        fragment.getSearch(v.text.toString())
                    } else if (fragment is HistoryFragment) {
                        fragment.getSearch(v.text.toString())
                    } else if (fragment is WalletFragment) {
                        fragment.getSearch(v.text.toString())
                    }
                } else {
                    Toast.makeText(this, "Search keyword must be at least 3 characters long", Toast.LENGTH_SHORT).show()
                }
            }
            false
        })
    }

    fun setFragment(fr: Fragment) {
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.container, fr)
        frag.addToBackStack(null)
        frag.commit()
    }

    private fun addFragmentToActivity(fragment: Fragment?) {
        if (fragment == null) return
        val frag = supportFragmentManager.beginTransaction()
        frag.add(R.id.container, fragment)
        frag.addToBackStack(null)
        frag.commitAllowingStateLoss()
    }

    fun replaceFragmentHomeFragment(fragment: Fragment?, tag: String?, isAddToBackStack: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment!!, tag)
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(tag)
        }
        fragmentTransaction.commit()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is HomeFragment1)
            finishAffinity()
        else if (fragment is GeoStoreFragment || fragment is FavFragment || fragment is HistoryFragment || fragment is WalletFragment) {
            replaceFragmentHomeFragment(HomeFragment1(), "HomeFragment", false)
            bottomNavItemSelected(true, false, false, false, false)
        } else finishAffinity()
    }

    fun bottomNavItemSelected(
        isHomeSelected: Boolean,
        isGeoStoreSelected: Boolean,
        isFavSelected: Boolean,
        isHistorySelected: Boolean,
        isWalletSelected: Boolean
    ) {
        if (isHomeSelected)
            homeBinding.layoutDashboard.bottomNavigationView.imageViewHome.setImageResource(R.drawable.active_home)
        else
            homeBinding.layoutDashboard.bottomNavigationView.imageViewHome.setImageResource(R.drawable.not_active_home)

        if (isGeoStoreSelected)
            homeBinding.layoutDashboard.bottomNavigationView.imageViewGeoStore.setImageResource(R.drawable.active_geostore)
        else
            homeBinding.layoutDashboard.bottomNavigationView.imageViewGeoStore.setImageResource(R.drawable.not_active_geostore)

        if (isFavSelected)
            homeBinding.layoutDashboard.bottomNavigationView.imageViewFav.setImageResource(R.drawable.active_fav)
        else
            homeBinding.layoutDashboard.bottomNavigationView.imageViewFav.setImageResource(R.drawable.not_active_fav)

        if (isHistorySelected)
            homeBinding.layoutDashboard.bottomNavigationView.imageViewHistory.setImageResource(R.drawable.active_history)
        else
            homeBinding.layoutDashboard.bottomNavigationView.imageViewHistory.setImageResource(R.drawable.not_active_history)

        if (isWalletSelected)
            homeBinding.layoutDashboard.bottomNavigationView.imageViewWallet.setImageResource(R.drawable.active_wallet)
        else
            homeBinding.layoutDashboard.bottomNavigationView.imageViewWallet.setImageResource(R.drawable.not_active_wallet)
    }

    /**
     * Set the side navigation title's according to user type
     */
    fun setNavigationItem() {
        homeBinding.navigationView.imageViewEdit.setOnClickListener {
            val intent = Intent(this@HomeActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        homeBinding.navigationView.layoutImage.setOnClickListener {
            val intent = Intent(this@HomeActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        val navAdapter = NavigationAdapter(this, linkItem!!)
        homeBinding.navigationBar.recyclerViewNavMenu.layoutManager =
            LinearLayoutManager(this@HomeActivity)
        homeBinding.navigationBar.recyclerViewNavMenu.adapter = navAdapter
        navAdapter.setOnClickListener(object :
            NavigationAdapter.OnClickListener {
            override fun onItemClickListener(position: Int) {
                if (linkItem?.get(position)?.title.equals("Terms of Service")) {

                    val intent = Intent(this@HomeActivity, ActivityForFragments::class.java)
                    intent.putExtra("fragmentName", "TermsOfService")
                    startActivity(intent)

                } else if (linkItem?.get(position)?.title.equals("Privacy Policy")) {

                    val intent = Intent(this@HomeActivity, ActivityForFragments::class.java)
                    intent.putExtra("fragmentName", "PrivacyPolicy")
                    startActivity(intent)

                } else if (linkItem?.get(position)?.title.equals("Start App Tutorial")) {

                    showStartTutorialDialog()

                } else if (linkItem?.get(position)?.title.equals("Report an Issue")) {

                    val intent = Intent(this@HomeActivity, ReportAnIssueActivity::class.java)
                    startActivity(intent)

                } else if (linkItem?.get(position)?.title.equals("Logout")) {

                    logout()

                } else {

                    val intent = Intent(this@HomeActivity, WebViewActivity::class.java)
                    intent.putExtra("title", linkItem?.get(position)?.title)
                    intent.putExtra("url", linkItem?.get(position)?.url)
                    startActivity(intent)
                }
            }
        })
    }

    private fun showStartTutorialDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(R.string.start_tutorial_title)
        dialogBuilder.setMessage(R.string.start_tutorial_message)
        dialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            homeBinding.drawar.closeDrawer(Gravity.LEFT);

            showGuide()

            /*val homeFragment1: HomeFragment1 = supportFragmentManager.findFragmentById(R.id.container) as HomeFragment1
            homeFragment1.showGuide()*/

            /*val intent = Intent(this@HomeActivity, ActivityForFragments::class.java)
                intent.putExtra("fragmentName", "AppTutorial")
                startActivity(intent)*/
        }
        dialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        val textView = alertDialog.window?.findViewById(android.R.id.message) as TextView
        val alertTitle = alertDialog.window?.findViewById(R.id.alertTitle) as TextView
        val button1 = alertDialog.window?.findViewById(android.R.id.button1) as Button
        val button2 = alertDialog.window?.findViewById(android.R.id.button2) as Button

        textView.typeface = App.getResFont(R.font.lato_regular)
        alertTitle.typeface = App.getResFont(R.font.lato_bold)
        button1.typeface = App.getResFont(R.font.lato_regular)
        button2.typeface = App.getResFont(R.font.lato_regular)
    }

    fun logout() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(R.string.log_out)
        dialogBuilder.setMessage(R.string.are_you_sure_want_to_logout)
        dialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            App.mLocalStore?.clearDataOnLogout()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        dialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        val textView = alertDialog.window?.findViewById(android.R.id.message) as TextView
        val alertTitle = alertDialog.window?.findViewById(R.id.alertTitle) as TextView
        val button1 = alertDialog.window?.findViewById(android.R.id.button1) as Button
        val button2 = alertDialog.window?.findViewById(android.R.id.button2) as Button

        textView.typeface = App.getResFont(R.font.lato_regular)
        alertTitle.typeface = App.getResFont(R.font.lato_bold)
        button1.typeface = App.getResFont(R.font.lato_regular)
        button2.typeface = App.getResFont(R.font.lato_regular)
    }

    override fun onResume() {
        super.onResume()
        if (App.mLocalStore?.getUserProfile() != null) {
            try {
                homeBinding.navigationView.textViewUserName.text =
                    App.mLocalStore?.getUserProfile()!!.first_name + " " + App.mLocalStore?.getUserProfile()!!.last_name
                Glide.with(this).load(App.mLocalStore?.getUserProfile()!!.profile_pic)
                    .apply(RequestOptions.placeholderOf(R.drawable.user_placeholder).dontAnimate())
                    .into(homeBinding.navigationView.imageViewUserProfile)
                Glide.with(this).load(App.mLocalStore?.getUserProfile()!!.profile_pic)
                    .apply(RequestOptions.placeholderOf(R.drawable.user_placeholder).dontAnimate())
                    .into(homeBinding.layoutDashboard.layoutHeader.imageViewProfile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun showGuide() {
        Handler().postDelayed({
            firstGuide()
        }, Constants.GUIDE_DELAY)
    }

    private fun firstGuide() {
        guideView = GuideView.Builder(this)
            .setTitle("The Home tab always returns you to this screen")
            .setTitleTextSize(14)
            .setGravity(smartdevelop.ir.eram.showcaseviewlib.config.Gravity.center)
            .setTargetView(homeBinding.layoutDashboard.bottomNavigationView.imageViewHome)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                secondGuide()
            }
            .build()
        guideView?.show()
    }

    private fun secondGuide() {
        guideView = GuideView.Builder(this)
            .setTitle("The GeoStore tab is under development.")
            .setTitleTextSize(14)
            .setGravity(smartdevelop.ir.eram.showcaseviewlib.config.Gravity.center)
            .setTargetView(homeBinding.layoutDashboard.bottomNavigationView.imageViewGeoStore)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                thirdGuide()
            }
            .build()
        guideView?.show()
    }

    private fun thirdGuide() {
        guideView = GuideView.Builder(this)
            .setTitle("The Favourites tab is under development.")
            .setTitleTextSize(14)
            .setGravity(smartdevelop.ir.eram.showcaseviewlib.config.Gravity.center)
            .setTargetView(homeBinding.layoutDashboard.bottomNavigationView.imageViewFav)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                fourthGuide()
            }
            .build()
        guideView?.show()
    }

    private fun fourthGuide() {
        guideView = GuideView.Builder(this)
            .setTitle("The History tab is under development.")
            .setTitleTextSize(14)
            .setGravity(smartdevelop.ir.eram.showcaseviewlib.config.Gravity.center)
            .setTargetView(homeBinding.layoutDashboard.bottomNavigationView.imageViewHistory)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                fivethGuide()
            }
            .build()
        guideView?.show()
    }

    private fun fivethGuide() {
        guideView = GuideView.Builder(this)
            .setTitle("The Wallet tab is under development.")
            .setTitleTextSize(14)
            .setGravity(smartdevelop.ir.eram.showcaseviewlib.config.Gravity.center)
            .setTargetView(homeBinding.layoutDashboard.bottomNavigationView.imageViewWallet)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {

            }
            .build()
        guideView?.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}