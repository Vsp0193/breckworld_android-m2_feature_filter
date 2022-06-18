package com.breckworld.ui.main.offerViewDialog

import android.os.Bundle
import android.os.Handler
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMVRPlayerFragment
import com.breckworld.livedata.EventObserver
import com.breckworld.databinding.FragmentOfferViewBinding
import com.breckworld.extensions.Constants
import com.breckworld.repository.database.model.OfferDB
import com.breckworld.utils.Utils
/*import com.breckworld.view.omnivirt.OmniVirtVRPlayerFragment*/
import com.google.android.material.floatingactionbutton.FloatingActionButton
/*import com.omnivirt.vrkit.Mode*/
import kotlinx.android.synthetic.main.fragment_offer_view.*

class OfferViewFragment :
    BaseMVVMVRPlayerFragment<OfferViewViewModel, FragmentOfferViewBinding, OfferViewFragment.Events>() {

    override fun viewModelClass(): Class<OfferViewViewModel> = OfferViewViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_offer_view

 //   lateinit var vrPlayer: OmniVirtVRPlayerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            (getSerializable(Constants.KEY_OFFER) as OfferDB?)?.let {
                viewModel.initData(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(false)
        setupStatusbar(true, false)
        setupStatusBarPadding(true)
        initView()
    }

    private fun initView() {
        text_view_title?.text = viewModel.title
        text_view_description?.text = viewModel.description
        text_view_address?.text = viewModel.fullAddress

        floating_action_button_close?.setOnClickListener {
            findNavController().navigateUp()
        }

       // if (viewModel.isVideoExist) {
       //     frame_layout_media.visibility = View.VISIBLE
        //    setupVRPlayer()
       // } else {
            image_view_photo.visibility = View.VISIBLE
            Utils.setImageFromUrl(viewModel.photoUrl, viewModel.placeholderId, image_view_photo)
       // }
    }

    fun setupStatusBarPadding(enablePadding: Boolean) {
        val top = if (enablePadding) getStatusBarHeight() else 0
        frame_layout_dialog.setPadding(0, top, 0, 0)
    }

    /*
    private fun setupVRPlayer() {
        context?.let {
          vrPlayer = OmniVirtVRPlayerFragment.newInstance()
            val flMedia = FrameLayout(it)
            flMedia.id = R.id.fl_media_player
            flMedia.layoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            frame_layout_media.addView(flMedia)
            childFragmentManager.beginTransaction()
                .apply {
                    add(R.id.fl_media_player, vrPlayer)
                    commit()
                }
            Handler().postDelayed({
                vrPlayer.load(viewModel.videoId)
                vrPlayer.cardboard = Mode.OFF
            }, 200)
        }
    }

    override fun onVRPlayerExpanded() {
        activity?.runOnUiThread {
            val root = binding.root
            val flMediaPlayer = root.findViewById<FrameLayout>(R.id.fl_media_player)
            val clDialog = root.findViewById<FrameLayout>(R.id.frame_layout_dialog)
            clDialog.removeAllViews()
            if (flMediaPlayer.parent != null) (flMediaPlayer.parent as ViewGroup).removeView(flMediaPlayer)
            flMediaPlayer.layoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            clDialog.addView(flMediaPlayer)
            setupStatusBarPadding(false)
            TransitionManager.beginDelayedTransition(clDialog)
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
            val flDialog = LayoutInflater.from(context).inflate(R.layout.fragment_offer_view, null) as ViewGroup
            flDialog.setPadding(0, getStatusBarHeight(), 0, 0)
            root.addView(flDialog)
            val flMedia = flDialog.findViewById<FrameLayout>(R.id.frame_layout_media)
            flMedia.visibility = View.VISIBLE
            flMedia.addView(flMediaPlayer)

            val closeButton: FloatingActionButton = flDialog.findViewById(R.id.floating_action_button_close)
            closeButton.setOnClickListener {
                viewModel.closeDialog()
            }

            val tvTitle: TextView = flDialog.findViewById(R.id.text_view_title)
            tvTitle.text = viewModel.title
            val tvDescription: TextView = flDialog.findViewById(R.id.text_view_description)
            tvDescription.text = viewModel.description
            val tvAddress: TextView = flDialog.findViewById(R.id.text_view_address)
            tvAddress.text = viewModel.fullAddress
            val ivPhoto: ImageView = flDialog.findViewById(R.id.image_view_photo)
            ivPhoto.visibility = View.GONE

            val fabClose: FloatingActionButton = flDialog.findViewById(R.id.floating_action_button_close)
            fabClose.setOnClickListener {
                findNavController().navigateUp()
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
}