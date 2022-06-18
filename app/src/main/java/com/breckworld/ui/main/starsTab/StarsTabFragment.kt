package com.breckworld.ui.main.starsTab

import android.annotation.SuppressLint

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.architecture.FragmentsPagerAdapterWithoutTitle
import com.breckworld.databinding.FragmentStarsTabBinding
import com.breckworld.extensions.Constants
import com.breckworld.livedata.EventObserver
import com.breckworld.ui.main.starsTab.collectedStars.CollectedStarsFragment
import com.breckworld.ui.main.starsTab.starsClues.StarsCluesFragment
import kotlinx.android.synthetic.main.fragment_stars_tab.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity

/**
 * @author Dmytro Bondarenko
 *         Date: 30.05.2019
 *         Time: 11:01
 *         E-mail: bondes87@gmail.com
 */
class StarsTabFragment :
    BaseMVVMFragment<StarsTabViewModel, FragmentStarsTabBinding, StarsTabFragment.Events>() {

    override fun viewModelClass(): Class<StarsTabViewModel> = StarsTabViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_stars_tab

    var guideView: GuideView? = null

    var openTab = 0

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openTab = arguments?.getInt(Constants.KEY_TAB) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPager()
        showBottomNavigation(true)
        setupStatusbar(true, true)
        setupStatusBarPadding()
        showGuide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        guideView?.dismiss()
    }

    private fun setupStatusBarPadding() {
        val top = constraint_layout_content.paddingTop + getStatusBarHeight()
        val bottom = constraint_layout_content.paddingBottom
        val start = constraint_layout_content.paddingStart
        val end = constraint_layout_content.paddingEnd
        constraint_layout_content.setPadding(start, top, end, bottom)
    }

    private fun setPager() {
        val adapter = FragmentsPagerAdapterWithoutTitle(childFragmentManager)
        adapter.addFragment(StarsCluesFragment())
        adapter.addFragment(CollectedStarsFragment())
        view_pager_stars?.adapter = adapter
        tab_layout?.setupWithViewPager(view_pager_stars, true)
        setTabs()
        view_pager_stars.currentItem = openTab
    }

    private fun setTabs() {
        setTab(0, R.string.star_clues, R.drawable.selector_tab_left_rounded)
        setTab(1, R.string.collected_stars, R.drawable.selector_tab_right_rounded)
    }

    @SuppressLint("InflateParams")
    private fun setTab(position: Int, @StringRes stringId: Int, @DrawableRes drawableId: Int) {
        val tab = LayoutInflater.from(context).inflate(R.layout.item_star_tab, null) as FrameLayout
        (tab.findViewById<View>(R.id.text_view_title) as TextView).text = getString(stringId)
        tab.background = ContextCompat.getDrawable(context!!, drawableId)
        tab_layout?.getTabAt(position)!!.customView = tab
        val parentView = tab_layout?.getTabAt(position)!!.customView?.parent as LinearLayout
        parentView.setPadding(0, 0, 0, 0)
    }

    private fun showGuide() {
        if (viewModel.isGuideStars()) {
            Handler().postDelayed({
                starCluesGuide()
            }, Constants.GUIDE_DELAY)
        }
    }

    private fun starCluesGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_stats_1))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(tab_layout.getTabAt(0)?.customView)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    collectedStarsGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun collectedStarsGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_stats_2))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(tab_layout.getTabAt(1)?.customView)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    viewModel.saveGuideStars(false)
                }
            }
            .build()
        guideView?.show()
    }

    enum class Events {}
}