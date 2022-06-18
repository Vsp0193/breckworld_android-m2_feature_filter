package com.breckworld.ui.main.offerdialog

import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMVRPlayerFragment
import com.breckworld.livedata.EventObserver
import com.breckworld.databinding.FragmentOfferDialogBinding
import com.breckworld.extensions.Constants
import com.breckworld.ui.main.offerdialog.OfferDialogViewModel.Companion.CATEGORY_ATTRACTIONS
import com.breckworld.ui.main.offerdialog.OfferDialogViewModel.Companion.CATEGORY_EAT_AND_DRINK
import com.breckworld.ui.main.offerdialog.OfferDialogViewModel.Companion.CATEGORY_FREE_FUN
import com.breckworld.ui.main.offerdialog.OfferDialogViewModel.Companion.CATEGORY_SPECIAL_OFFER
//import com.breckworld.view.omnivirt.OmniVirtVRPlayerFragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.omnivirt.vrkit.Mode
import kotlinx.android.synthetic.main.fragment_offer_dialog.*

class OfferDialogFragment :
    BaseMVVMVRPlayerFragment<OfferDialogViewModel, FragmentOfferDialogBinding, OfferDialogFragment.Events>() {

//    lateinit var vrPlayer: OmniVirtVRPlayerFragment

    override fun viewModelClass(): Class<OfferDialogViewModel> {
        return OfferDialogViewModel::class.java
    }

    override fun layoutResId(): Int = R.layout.fragment_offer_dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            getSerializable(Constants.KEY_OFFER).let {
                viewModel.initData(it)
            }
            viewModel.location = getParcelable(Constants.KEY_LOCATION) as? Location
        }
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
        tv_title?.text = viewModel.title
        tv_description?.text = viewModel.description
        tv_address?.text = viewModel.fullAddress
        when (viewModel.category) {
            CATEGORY_EAT_AND_DRINK -> iv_category?.setImageResource(R.drawable.ic_category_eat_and_drink)
            CATEGORY_FREE_FUN -> iv_category?.setImageResource(R.drawable.ic_category_free_and_fun)
            CATEGORY_ATTRACTIONS -> iv_category?.setImageResource(R.drawable.ic_category_attraction)
            CATEGORY_SPECIAL_OFFER -> iv_category?.setImageResource(R.drawable.ic_offer)
            else -> iv_category?.setImageResource(R.drawable.ic_category_eat_and_drink)
        }
        btn_pick_offer?.setOnClickListener {
            viewModel.collectOffer()
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
            val flDialog = LayoutInflater.from(context).inflate(R.layout.fragment_offer_dialog, null) as ViewGroup
            flDialog.setPadding(0, getStatusBarHeight(), 0, 0)
            root.addView(flDialog)
            val flMedia = flDialog.findViewById<FrameLayout>(R.id.fl_media)
            flMedia.addView(flMediaPlayer)

            val closeButton: FloatingActionButton = flDialog.findViewById(R.id.btn_close)
            closeButton.setOnClickListener {
                viewModel.closeDialog()
            }

            val tvTitle: TextView = flDialog.findViewById(R.id.tv_title)
            tvTitle.text = viewModel.title
            val tvDescription: TextView = flDialog.findViewById(R.id.tv_description)
            tvDescription.text = viewModel.description
            val tvAddress: TextView = flDialog.findViewById(R.id.tv_address)
            tvAddress.text = viewModel.fullAddress
            val ivCategory: ImageView = flDialog.findViewById(R.id.iv_category)
            when (viewModel.category) {
                CATEGORY_EAT_AND_DRINK -> ivCategory.setImageResource(R.drawable.ic_category_eat_and_drink)
                CATEGORY_FREE_FUN -> ivCategory.setImageResource(R.drawable.ic_category_free_and_fun)
                CATEGORY_ATTRACTIONS -> ivCategory.setImageResource(R.drawable.ic_category_attraction)
                else -> ivCategory.setImageResource(R.drawable.ic_category_eat_and_drink)
            }
            val btnPickOffer: MaterialButton = flDialog.findViewById(R.id.btn_pick_offer)
            btnPickOffer.setOnClickListener {
                viewModel.collectOffer()
            }
            TransitionManager.beginDelayedTransition(flDialog)
        }
    }
*/
    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {
            when (it) {
                Events.BACK -> findNavController().navigateUp()
                Events.OFFER_COLLECTED -> findNavController().popBackStack(R.id.action_arview, false)
            }
        })
    }

    enum class Events {
        BACK,
        OFFER_COLLECTED
    }

    companion object {
        fun newInstance(args: Bundle? = null): OfferDialogFragment {
            val fragment = OfferDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

}