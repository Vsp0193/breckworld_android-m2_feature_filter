package com.breckworld.ui.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.livedata.EventObserver
import com.breckworld.databinding.FragmentHomeBinding
import com.breckworld.extensions.*
import com.breckworld.repository.local.model.preview.PreviewModel
import com.breckworld.repository.local.model.preview.SearchPreviewModel
import com.breckworld.repository.local.model.preview.VideoPreviewModel
import com.breckworld.ui.main.MainActivity
import com.breckworld.ui.main.home.preview_adapter.HomePreviewRvAdapter
import com.breckworld.view.HorizontalSpaceMarginItemDecoration
//import com.breckworld.view.omnivirt.OmniVirtVRPlayerFragment
//import com.omnivirt.vrkit.Mode
//import com.omnivirt.vrkit.OnVRPlayerInteractionListener
//import com.omnivirt.vrkit.Quality
import kotlinx.android.synthetic.main.fragment_home.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import java.lang.reflect.Array

private const val CONTENT_ID = 24825

class HomeFragment : BaseMVVMFragment<HomeViewModel, FragmentHomeBinding, HomeFragment.Events>()/*,
    OnVRPlayerInteractionListener*/ {

    var guideView: GuideView? = null

    private val townsAdapter = HomePreviewRvAdapter(
        mutableListOf(
            VideoPreviewModel(
                "Swaffham",
                "http://www.swaffhamtowncouncil.gov.uk",
                "Swaffham",
                "OijXn_6IQ_E",
                "https://viewing.online/wp-content/uploads/gravity_forms/3-8dccc1e69cba5471f8e4b403556cdbb3/2018/09/swaffham-square.jpg",
                "Swaffham 360 Exp.",
                "Swaffham is a boutique market town offering a variety of fun and cuisine. It's home to the wonderful Stephen Fry so pop along and who knows, maybe you'll meet him!",
                "Swaffham, Norfolk",
                52.681618,
                0.93782699999999996,
                10,
                R.drawable.ic_swaffham
            ),
            VideoPreviewModel(
                "Thetford",
                "http://www.thetfordtowncouncil.gov.uk",
                "Thetford",
                "duVSM6Br-WU",
                "https://viewing.online/wp-content/uploads/gravity_forms/3-8dccc1e69cba5471f8e4b403556cdbb3/2018/09/thetford-square.jpg",
                "Thetford 360 Exp.",
                "Thetford has so much to offer! Shopping, coffee culture, trails, history and heritage, it's all here wrapped up in one lovely package.",
                "Thetford, Norfolk",
                52.681618,
                0.93782699999999996,
                10,
                R.drawable.ic_thetford
            ),
            VideoPreviewModel(
                "Dereham",
                "http://derehamtc.norfolkparishes.gov.uk",
                "Dereham",
                "YxXIsuUKB2U",
                "https://viewing.online/wp-content/uploads/gravity_forms/3-8dccc1e69cba5471f8e4b403556cdbb3/2018/09/dereham-360-square.jpg",
                "Dereham 360 Exp.",
                "Dereham has got something for everybody. Whether it's history, trails or fun for the family, you'll find it here in the very centre of Norfolk.",
                "Dereham, Norfolk",
                52.681618,
                0.93782699999999996,
                10,
                R.drawable.ic_dereham
            ),
            VideoPreviewModel(
                "Attleborough",
                "https://attleboroughtc.org.uk",
                "Attleborough",
                "32krvAmw0EM",
                "https://viewing.online/wp-content/uploads/gravity_forms/3-8dccc1e69cba5471f8e4b403556cdbb3/2018/09/attleborough-360-exp-square.jpg",
                "Attleborough 360 Exp.",
                "Virtually visit Attleborough, learn about the town's history and culture and see all of the best things to do in and around the town.",
                "Attleborough, Norfolk",
                52.681618,
                0.93782699999999996,
                10,
                R.drawable.ic_attleborough
            ),
            VideoPreviewModel(
                "Watton",
                "http://www.wattontowncouncil.gov.uk",
                "Watton",
                "DFHYIPqaQtU",
                "https://viewing.online/wp-content/uploads/gravity_forms/3-8dccc1e69cba5471f8e4b403556cdbb3/2018/09/watton-square.jpg",
                "Watton 360 Exp.",
                "Watton is a market town with an abundance of history and a story to tell. It's the setting of the infamous Babes In The Wood tale. Come and explore!",
                "Watton, Norfolk",
                52.681618,
                0.93782699999999996,
                10,
                R.drawable.ic_watton
            )
        ), object : HomePreviewRvAdapter.Listener<PreviewModel> {
            override fun onItemClick(item: PreviewModel) {
                when (item) {
                    is VideoPreviewModel -> {
                        val bundle = bundleOf(Constants.KEY_ANY to item)
                        findNavController().navigateTo(R.id.action_global_to_star_dialog, bundle)
                    }
                    is SearchPreviewModel -> {
                        val bundle = bundleOf(Constants.KEY_SEARCH to Constants.SEARCH_FUN)
                        findNavController().navigateTo(R.id.action_search, bundle)
                    }
                }
            }
        }
    )

    private val funAdapter = HomePreviewRvAdapter(
        mutableListOf(
            VideoPreviewModel(
                "Gressenhall Farm",
                "https://www.museums.norfolk.gov.uk/gressenhall-farm-and-workhouse",
                "Gressenhall Farm",
                "52qZa29zPHA",
                "https://viewing.online/wp-content/uploads/gravity_forms/3-8dccc1e69cba5471f8e4b403556cdbb3/2018/09/hressenhall-square.jpg",
                "Gressenhall Farm 360 Exp.",
                "A great day out for kids and adults alike, Gressenhall Farm is a real hands-on experience that you won't forget in a hurry. Watch our 360 Experience to see for yourself!",
                "Gressenhall, Dereham, NR20 4DR",
                52.681618,
                0.93782699999999996,
                3,
                R.drawable.ic_gressenhall_farm
            ),
            VideoPreviewModel(
                "Banham Zoo",
                "https://www.banhamzoo.co.uk",
                "Banham Zoo",
                "vGtrcDB-I9A",
                "https://viewing.online/wp-content/uploads/gravity_forms/3-8dccc1e69cba5471f8e4b403556cdbb3/2018/09/banham-zoo-550.jpg",
                "Banham Zoo 360 Exp.",
                "What's your favourite animal? We're sure you'll find them here at Banham Zoo plus hundreds of others! Come and enjoy an awesome day out with us!",
                "Kenninghall Road, Banham, NR16 2HE",
                52.681618,
                0.93782699999999996,
                3,
                R.drawable.ic_banham_zoo
            ),
            VideoPreviewModel(
                "Oxburgh Hall",
                "https://www.nationaltrust.org.uk/oxburgh-hall",
                "Oxburgh Hall",
                "lthfOe0MzuM",
                "https://viewing.online/wp-content/uploads/gravity_forms/3-8dccc1e69cba5471f8e4b403556cdbb3/2018/09/oxburgh-square.jpg",
                "Oxburgh Hall 360 Exp.",
                "Explore over 500 years of rich history at the incredible Oxburgh Hall. This National Trust site is one of Norfolk's hidden gems so come and uncover it.",
                "Oxborough, Swaffham, PE33 9PS",
                52.681618,
                0.93782699999999996,
                3,
                R.drawable.ic_oxburgh_hall
            ),
            VideoPreviewModel(
                "Melsop Farm",
                "http://www.melsopfarmpark.co.uk",
                "Melsop Farm",
                "JyKHICpNCjE",
                "https://viewing.online/wp-content/uploads/gravity_forms/3-8dccc1e69cba5471f8e4b403556cdbb3/2018/09/melsop-farm-square.jpg",
                "Melsop Farm 360 Exp.",
                "Spend some time at the friendliest attraction in Norfolk. Melsop Farm allows you to get hands on with all kinds of animals and we guarantee a great time!",
                "Ellingham Road, Scoulton, Watton, NR9 4NT",
                52.681618,
                0.93782699999999996,
                3,
                R.drawable.ic_melsop_farm
            ),
            VideoPreviewModel(
                "Mid Norfolk Railway",
                "http://www.mnr.org.uk",
                "Mid Norfolk Railway",
                "ldDe2d90LaQ",
                "https://viewing.online/wp-content/uploads/gravity_forms/3-8dccc1e69cba5471f8e4b403556cdbb3/2018/09/mnr-square-2.jpg",
                "Mid Norfolk Railway 360 Exp.",
                "The Mid Norfolk Railway provides a really special experience for people of all ages. The famous Cream Tea Service is always popular but there are many more events.",
                "ereham Station, Station Road, Dereham, NR19 1DF",
                52.681618,
                0.93782699999999996,
                3,
                R.drawable.ic_mid_norfolk_railway
            ),
            SearchPreviewModel()
        ), object : HomePreviewRvAdapter.Listener<PreviewModel> {
            override fun onItemClick(item: PreviewModel) {
                when (item) {
                    is VideoPreviewModel -> {
                        val bundle = bundleOf(Constants.KEY_ANY to item)
                        findNavController().navigateTo(R.id.action_global_to_star_dialog, bundle)
                    }
                    is SearchPreviewModel -> {
                        val bundle = bundleOf(Constants.KEY_SEARCH to Constants.SEARCH_FUN)
                        findNavController().navigateTo(R.id.action_search, bundle)
                    }
                }
            }
        }
    )

    override fun viewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun layoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(true)
        setupTownsRecycler()
        setupFunRecycler()
//        val vrPlayer = childFragmentManager.findFragmentById(R.id.vrplayer) as? OmniVirtVRPlayerFragment
//        vrPlayer?.load(CONTENT_ID)
        showGuide()


        vrplayer.setOnClickListener {
            // your code to perform when the user clicks on the ImageView
            try {
                var vid = "fN1iGhyOOQg" //the video id of the youtube video
                var url = "vnd.youtube:$vid"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {
                val text = "Please install the YouTube App for the best experience. When inside the YouTube app, please select the highest quality playback from the settings button and click the VR goggles button to go into Virtual Reality mode.\n"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(requireContext(), text, duration)
                toast.show()
            }
        }
    }



    private fun setupTownsRecycler() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val decorator = HorizontalSpaceMarginItemDecoration(0, 0, 2f.toPx())
        rv_towns.adapter = townsAdapter
        rv_towns.layoutManager = layoutManager
        rv_towns.addItemDecoration(decorator)
    }

    private fun setupFunRecycler() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val decorator = HorizontalSpaceMarginItemDecoration(0, 0, 2f.toPx())
        rv_fun.adapter = funAdapter
        rv_fun.layoutManager = layoutManager
        rv_fun.addItemDecoration(decorator)
    }


/*    override fun onVRPlayerCollapsed() {
        activity?.runOnUiThread {
            cl_content.visibility = View.VISIBLE
            showBottomNavigation(true)
        }
    }

    override fun onVRPlayerExpanded() {
        activity?.runOnUiThread {
            cl_content.visibility = View.GONE
            showBottomNavigation(false)
        }
    }

    override fun onVRPlayerCardboardChanged(p0: Mode?) {}
    override fun onVRPlayerEnded() {}
    override fun onVRPlayerDurationChanged(p0: Double?) {}
    override fun onVRPlayerProgressChanged(p0: Double?) {}
    override fun onVRPlayerFragmentCreated() {}
    override fun onVRPlayerPaused() {}
    override fun onVRPlayerLongitudeChanged(p0: Double?) {}
    override fun onVRPlayerSkipped() {}
    override fun onVRPlayerStarted() {}
    override fun onVRPlayerLatitudeChanged(p0: Double?) {}
    override fun onVRPlayerSwitched(p0: String?, p1: Array?) {}
    override fun onVRPlayerLoaded(p0: Int?, p1: Quality?, p2: Mode?) {}
    override fun onVRPlayerVolumeChanged(p0: Double?) {}
    override fun onVRPlayerQualityChanged(p0: Quality?) {}
    override fun onVRPlayerBufferChanged(p0: Double?) {}
    override fun onVRPlayerSeekChanged(p0: Double?) {}
*/
    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {
            when (it) {
                Events.BACK -> findNavController().navigateUp()
            }
        })
    }

    private fun showGuide() {
        if (viewModel.isGuideHome()) {
            Handler().postDelayed({
                vrplayerGuide()
            }, Constants.GUIDE_DELAY)
        }
    }

    private fun vrplayerGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_home_1))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(view_guide_vrplayer)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    vrplayerGuideContinue()
                }
            }
            .build()
        guideView?.show()
    }

    private fun vrplayerGuideContinue() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_home_2))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(view_guide_vrplayer)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    townsGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun townsGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_home_3))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(rv_towns)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    funGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun funGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_home_4))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(rv_fun)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    actionHomeGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun actionHomeGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_home_5))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView((activity as MainActivity).getBottomBarItem(R.id.action_home))
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    actionSearchGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun actionSearchGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_home_6))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView((activity as MainActivity).getBottomBarItem(R.id.action_search))
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    actionArviewGuide()
                }
            }
            .build()
        guideView?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        guideView?.dismiss()
    }

    private fun actionArviewGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_home_7))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView((activity as MainActivity).getBottomBarItem(R.id.action_arview))
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    actionStarsGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun actionStarsGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_home_8))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView((activity as MainActivity).getBottomBarItem(R.id.action_stars))
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    actionWalletGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun actionWalletGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_home_9))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView((activity as MainActivity).getBottomBarItem(R.id.action_wallet))
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    viewModel.saveGuideHome(false)
                }
            }
            .build()
        guideView?.show()
    }

    enum class Events {
        BACK
    }

    companion object {
        fun newInstance(args: Bundle? = null): HomeFragment {
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

}