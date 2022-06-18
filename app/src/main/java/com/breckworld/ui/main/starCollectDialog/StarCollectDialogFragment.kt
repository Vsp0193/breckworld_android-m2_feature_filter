package com.breckworld.ui.main.starCollectDialog

import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMVRPlayerFragment
import com.breckworld.livedata.EventObserver
import com.breckworld.databinding.FragmentStarCollectDialogBinding
import com.breckworld.extensions.Constants
import com.breckworld.extensions.navigateTo
//import com.breckworld.view.omnivirt.OmniVirtVRPlayerFragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.omnivirt.vrkit.Mode
import kotlinx.android.synthetic.main.fragment_star_collect_dialog.*
import kotlinx.android.synthetic.main.fragment_star_collect_dialog.fl_dialog

class StarCollectDialogFragment :
    BaseMVVMVRPlayerFragment<StarCollectDialogViewModel, FragmentStarCollectDialogBinding, StarCollectDialogFragment.Events>() {

//    lateinit var vrPlayer: OmniVirtVRPlayerFragment

    override fun viewModelClass(): Class<StarCollectDialogViewModel> {
        return StarCollectDialogViewModel::class.java
    }

    override fun layoutResId(): Int = R.layout.fragment_star_collect_dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            getSerializable(Constants.KEY_ANY).let {
                viewModel.initData(it)
            }
            getSerializable(Constants.KEY_LOCATION).let {
                viewModel.location = it as? Location
            }
        }
        viewModel.collectedStarsCount.observe(this, Observer {
            val count = it ?: 0
            val tvTitle: TextView? = view?.findViewById(R.id.tv_title)
            val tvCollectStar: TextView? = view?.findViewById(R.id.tv_collect_star)
            val collectText = App.getQuantityString(R.plurals.collected_star, count, count, viewModel.title)
            viewModel.collectText = collectText
            tvCollectStar?.text = viewModel.collectText
            val btnCollectedStars: MaterialButton? = view?.findViewById(R.id.btn_collected_stars)
            if (count % 10 == 0 && count > 0) {
                tvTitle?.text = App.getStringFromRes(R.string.ten_star_collected, count)
                btnCollectedStars?.text = App.getStringFromRes(R.string.select_special_offer)
                btnCollectedStars?.setOnClickListener {
                    val bundle = bundleOf(Constants.KEY_LOCATION to viewModel.location)
                    findNavController().navigateTo(R.id.action_collect_star_to_specialOfferFragment, bundle)
                }
            } else {
                tvTitle?.text = App.getStringFromRes(R.string.great_job)
                btnCollectedStars?.text = App.getStringFromRes(R.string.see_collected_stars)
                btnCollectedStars?.setOnClickListener {
                    val bundle = bundleOf(Constants.KEY_TAB to COLLECTED_STARS)
                    findNavController().navigateTo(R.id.action_collect_star_to_starsFragment, bundle)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(false)
        setupStatusbar(true, false)
        setupStatusBarPadding(true)
        initView()
        setupVRPlayer()
    }

    private fun initView() {
        val count = viewModel.collectedStarsCount.value ?: 0
        val tvTitle: TextView? = view?.findViewById(R.id.tv_title)
        val tvCollectStar: TextView? = view?.findViewById(R.id.tv_collect_star)
        tvCollectStar?.text = viewModel.collectText
        val btnCollectedStars: MaterialButton? = view?.findViewById(R.id.btn_collected_stars)
        if (count % 10 == 0 && count > 0) {
            tvTitle?.text = App.getStringFromRes(R.string.ten_star_collected, count)
            btnCollectedStars?.text = App.getStringFromRes(R.string.select_special_offer)
            btnCollectedStars?.setOnClickListener {
                val bundle = bundleOf(Constants.KEY_LOCATION to viewModel.location)
                findNavController().navigateTo(R.id.action_collect_star_to_specialOfferFragment, bundle)
            }
        } else {
            tvTitle?.text = App.getStringFromRes(R.string.great_job)
            btnCollectedStars?.text = App.getStringFromRes(R.string.see_collected_stars)
            btnCollectedStars?.setOnClickListener {
                val bundle = bundleOf(Constants.KEY_TAB to COLLECTED_STARS)
                findNavController().navigateTo(R.id.action_collect_star_to_starsFragment, bundle)
            }
        }
    }

    fun setupStatusBarPadding(enablePadding: Boolean) {
        val top = if (enablePadding) getStatusBarHeight() else 0
        fl_dialog.setPadding(0, top, 0, 0)
    }

    fun setupVRPlayer() {
        context?.let {
            val flMedia = FrameLayout(it)
            flMedia.id = R.id.fl_media_player
            flMedia.layoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            fl_media.addView(flMedia)
            //if (viewModel.videoId == 0) {
                val imageView = ImageView(it)
                imageView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                flMedia.addView(imageView)
                imageView.setBackgroundColor(App.getResColor(R.color.colorBlack))
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                if (viewModel.imageUrl.isNotBlank()) {
                    Glide.with(it).load(viewModel.imageUrl).into(imageView)
                }
                it
            /*} else {
                vrPlayer = OmniVirtVRPlayerFragment.newInstance()
                childFragmentManager.beginTransaction()
                    .apply {
                        add(R.id.fl_media_player, vrPlayer)
                        commit()
                    }
                Handler().postDelayed({
                    vrPlayer.load(viewModel.videoId)
                    vrPlayer.cardboard = Mode.OFF
                }, 200)
            }*/
        }
    }
/*
    override fun onVRPlayerExpanded() {
        activity?.runOnUiThread {
            val root = binding.root
            val flMediaPlayer = root.findViewById<FrameLayout>(R.id.fl_media_player)
            val flDialog = root.findViewById<FrameLayout>(R.id.fl_dialog)
            flDialog.removeAllViews()
            if (flMediaPlayer.parent != null) (flMediaPlayer.parent as ViewGroup).removeView(flMediaPlayer)
            flMediaPlayer.layoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            flDialog.addView(flMediaPlayer)
            setupStatusBarPadding(false)
            TransitionManager.beginDelayedTransition(flDialog)
        }
    }

    override fun onVRPlayerCollapsed() {
        activity?.runOnUiThread {
            val root = binding.root as ViewGroup
            val flMediaPlayer = root.findViewById<FrameLayout>(R.id.fl_media_player)
            root.removeAllViews()
            if (flMediaPlayer.parent != null) (flMediaPlayer.parent as ViewGroup).removeView(flMediaPlayer)
            flMediaPlayer.layoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            val flDialog = LayoutInflater.from(context).inflate(R.layout.fragment_star_collect_dialog, null) as ViewGroup
            flDialog.setPadding(0, getStatusBarHeight(), 0, 0)
            root.addView(flDialog)
            val flMedia = flDialog.findViewById<FrameLayout>(R.id.fl_media)
            flMedia.addView(flMediaPlayer)

            val closeButton: FloatingActionButton = flDialog.findViewById(R.id.btn_close)
            closeButton.setOnClickListener {
                viewModel.closeDialog()
            }

            val tvTitle: TextView = flDialog.findViewById(R.id.tv_title)
            val tvCollectStar: TextView = flDialog.findViewById(R.id.tv_collect_star)
            val btnCollectedStars: MaterialButton = flDialog.findViewById(R.id.btn_collected_stars)

            val count = viewModel.collectedStarsCount.value ?: 0
            tvCollectStar.text = viewModel.collectText
            if (count % 10 == 0 && count > 0) {
                tvTitle.text = App.getStringFromRes(R.string.ten_star_collected, count)
                btnCollectedStars.text = App.getStringFromRes(R.string.select_special_offer)
                btnCollectedStars.setOnClickListener {
                    val bundle = bundleOf(Constants.KEY_LOCATION to viewModel.location)
                    findNavController().navigateTo(R.id.action_collect_star_to_specialOfferFragment, bundle)
                }
            } else {
                tvTitle.text = App.getStringFromRes(R.string.great_job)
                btnCollectedStars.text = App.getStringFromRes(R.string.see_collected_stars)
                btnCollectedStars.setOnClickListener {
                    val bundle = bundleOf(Constants.KEY_TAB to COLLECTED_STARS)
                    findNavController().navigateTo(R.id.action_collect_star_to_starsFragment, bundle)
                }
            }

            TransitionManager.beginDelayedTransition(flDialog)
        }
    }
*/

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {
            when (it) {
                Events.BACK -> findNavController().navigateUp()
            }
        })
    }

    enum class Events {
        BACK
    }

    companion object {
        fun newInstance(args: Bundle? = null): StarCollectDialogFragment {
            val fragment = StarCollectDialogFragment()
            fragment.arguments = args
            return fragment
        }

        const val COLLECTED_STARS = 1
    }

}