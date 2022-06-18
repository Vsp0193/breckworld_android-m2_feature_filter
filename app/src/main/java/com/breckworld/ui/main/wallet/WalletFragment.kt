package com.breckworld.ui.main.wallet

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.databinding.FragmentWalletBinding
import com.breckworld.extensions.Constants
import com.breckworld.extensions.navigateTo
import com.breckworld.extensions.toast
import com.breckworld.livedata.EventObserver
import com.breckworld.repository.database.model.OfferDB
import kotlinx.android.synthetic.main.fragment_wallet.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity


/**
 * @author Dmytro Bondarenko
 *         Date: 07.06.2019
 *         Time: 12:20
 *         E-mail: bondes87@gmail.com
 */
class WalletFragment :
    BaseMVVMFragment<WalletViewModel, FragmentWalletBinding, WalletFragment.Events>() {

    override fun viewModelClass(): Class<WalletViewModel> = WalletViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_wallet

    private var walletsRecyclerAdapter: WalletsRecyclerAdapter? = null
    private var walletsLayoutManager: LinearLayoutManager? = null

    var guideView: GuideView? = null

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
                Events.REFRESH_CLOSE -> swipe_refresh_layout_wallet.isRefreshing = false
                Events.SETTINGS -> findNavController().navigateTo(R.id.action_action_wallet_to_settingsFragment)
                Events.PROFILE -> findNavController().navigateTo(R.id.action_action_wallet_to_profileFragment)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(true)
        setupStatusbar(true, true)
        setupStatusBarPadding()
        initViews()
        viewModel.checkAndUpdateProfile(false)
        viewModel.profile.removeObservers(this)
        viewModel.profile.observe(this, Observer { profileDB ->
            profileDB?.let { viewModel.profileAvatar.value = it.profilePic }
        })
        viewModel.offers.removeObservers(this)
        viewModel.offers.observe(this, Observer {
            updateWalletsRecyclerAdapter()
        })
        showGuide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        guideView?.dismiss()
    }

    private fun setupStatusBarPadding() {
        val top = swipe_refresh_layout_wallet.paddingTop + getStatusBarHeight()
        val bottom = swipe_refresh_layout_wallet.paddingBottom
        val start = swipe_refresh_layout_wallet.paddingStart
        val end = swipe_refresh_layout_wallet.paddingEnd
        swipe_refresh_layout_wallet.setPadding(start, top, end, bottom)
    }

    private fun updateWalletsRecyclerAdapter() {
        if (walletsRecyclerAdapter == null) {
            initWalletsRecyclerAdapter()
        } else {
            onWalletsLoaded()
        }
    }

    private fun initWalletsRecyclerAdapter() {
        walletsRecyclerAdapter =
            WalletsRecyclerAdapter(viewModel.offers.value?.toMutableList() ?: ArrayList(),
                object : BaseRecyclerViewAdapter.Listener<OfferDB> {
                    override fun onItemClick(item: OfferDB) {
                        val bundle = bundleOf(Constants.KEY_OFFER to item)
                        if (item.redeemed == 0) {
                            findNavController().navigateTo(R.id.action_action_wallet_to_offerPreviewFragment, bundle)
                        } else {
                            findNavController().navigateTo(R.id.action_action_wallet_to_offerViewFragment, bundle)
                        }
                    }
                })
        recycler_view_offers?.adapter = walletsRecyclerAdapter
    }

    private fun onWalletsLoaded() {
        walletsRecyclerAdapter?.replace(viewModel.offers.value?.toMutableList())
        viewModel.isLoading.value = false
    }


    private var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val defaultOffset = 16f
            var delta = 0f
            val position = walletsLayoutManager?.findFirstVisibleItemPosition()
            if (position != null) {
                val view = walletsLayoutManager?.findViewByPosition(position)
                if (view != null) {
                    if (position == 0) {
                        delta = -view.top / defaultOffset
                        if (delta > 1f) delta = 1f
                        if (delta < 0f) delta = 0f
                    } else {
                        delta = 1f
                    }
                }
            }
            recycler_view_separator.alpha = delta
        }
    }

    private fun initViews() {
        walletsLayoutManager = LinearLayoutManager(context)
        recycler_view_offers?.layoutManager = walletsLayoutManager
        recycler_view_offers.addOnScrollListener(scrollListener)
        initWalletsRecyclerAdapter()
        swipe_refresh_layout_wallet?.apply {
            setOnRefreshListener {
                viewModel.checkAndUpdateProfile(true)
            }
            setColorSchemeResources(R.color.colorGreen)
        }
    }

    private fun showGuide() {
        if (viewModel.isGuideWallet()) {
            Handler().postDelayed({
                offersGuide()
            }, Constants.GUIDE_DELAY)
        }
    }

    private fun offersGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_wallet_1))
            .setTitleTextSize(14)
            .setIndicatorHeight(24f)
            .setsetPositionMessageView(true)
            .setGravity(Gravity.center)
            .setTargetView(view_guide_offers)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    avatarGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun avatarGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_wallet_2))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(image_view_avatar)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    menuGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun menuGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_wallet_3))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(image_view_menu)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    viewModel.saveGuideWallet(false)
                }
            }
            .build()
        guideView?.show()
    }

    enum class Events {
        REFRESH_CLOSE,
        SETTINGS,
        PROFILE
    }
}