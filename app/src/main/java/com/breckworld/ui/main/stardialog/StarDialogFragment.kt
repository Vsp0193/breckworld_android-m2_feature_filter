package com.breckworld.ui.main.stardialog

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMVRPlayerFragment
import com.breckworld.databinding.FragmentStarDialogBinding
import com.breckworld.extensions.Constants
import com.breckworld.livedata.EventObserver
//import com.breckworld.view.omnivirt.OmniVirtVRPlayerFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.omnivirt.vrkit.Mode
import kotlinx.android.synthetic.main.fragment_star_dialog.*
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.breckworld.App
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class StarDialogFragment :
    BaseMVVMVRPlayerFragment<StarDialogViewModel, FragmentStarDialogBinding, StarDialogFragment.Events>() {

 //   lateinit var vrPlayer: OmniVirtVRPlayerFragment

    override fun viewModelClass(): Class<StarDialogViewModel> {
        return StarDialogViewModel::class.java
    }

    override fun layoutResId(): Int {
        return R.layout.fragment_star_dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            getSerializable(Constants.KEY_ANY).let {
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
        setupVRPlayer()
    }

    private fun initView() {
        tv_title?.text = viewModel.title
        tv_description?.text = viewModel.description
        tv_address?.text = viewModel.fullAddress
        star_badge?.text = viewModel.badges.toString()
        btn_experience?.setOnClickListener {
            //onVRPlayerExpanded()
            var vid = viewModel.videoId
            try {
                var url = "vnd.youtube:$vid"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {
                val webIntent: Intent = Uri.parse("https://www.youtube.com/watch?v=$vid").let { webpage ->
                    Intent(Intent.ACTION_VIEW, webpage)
                }
                /*val text = "Please install the YouTube App for the best experience. When inside the YouTube app, please select the highest quality playback from the settings button and click the VR goggles button to go into Virtual Reality mode.\n"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(requireContext(), text, duration)
                toast.show()*/
            }
        }
        btn_website?.setOnClickListener {
            startWebSite()
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
            val flDialog = LayoutInflater.from(context).inflate(R.layout.fragment_star_dialog, null) as ViewGroup
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
            val starBadge: TextView = flDialog.findViewById(R.id.star_badge)
            starBadge.text = viewModel.badges.toString()
            val btnExperience: MaterialButton = flDialog.findViewById(R.id.btn_experience)
            val btnWebSite: MaterialButton = flDialog.findViewById(R.id.btn_website)
            btnExperience.setOnClickListener {
                onVRPlayerExpanded()
            }
            btnWebSite.setOnClickListener {
                startWebSite()
            }

            TransitionManager.beginDelayedTransition(flDialog)
        }
    }
*/
    private fun startWebSite() {
        if (viewModel.url.isNotBlank()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(viewModel.url)
            startActivity(intent)
        }
    }

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