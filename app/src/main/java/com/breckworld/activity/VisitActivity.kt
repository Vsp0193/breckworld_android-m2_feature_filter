package com.breckworld.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.viewpager2.widget.ViewPager2
import com.breckworld.App
import com.breckworld.adapter.MdsBottomIconsAdapter
import com.breckworld.adapter.MdsButtonsAdapter
import com.breckworld.adapter.VisitAdapter
import com.breckworld.adapter.VisitBannersAdapter
import com.breckworld.app.repository.ResponseListener
import com.breckworld.databinding.ActivityVisitBinding
import com.breckworld.fragment.GeoStoreFragment
import com.breckworld.model.mds.Banner
import com.breckworld.model.mds.Footer
import com.breckworld.model.mds.Slide
import com.breckworld.repository.Repository2
import com.breckworld.viewmodel.VisitVM
import com.breckworld.viewmodel.factory.VisitVMFactory
import com.breckworld.webservice.ApiClient
import com.breckworld.webservice.ApiInterface
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class VisitActivity : AppCompatActivity() {

    lateinit var visitBinding: ActivityVisitBinding
    private lateinit var visitVM: VisitVM
    private var apiService: ApiInterface? = null
    private var linkedMdsId: Int? = null
    private var slideId: Int? = null
    private var mobileNo: String = ""
    private var visitAdapter: VisitAdapter? = null
    private var mdsButtonsAdapter: MdsButtonsAdapter? = null
    private var fromPosition = 0
    private var buttonClickedPosition = 0
    private var cameFrom = ""

    private var fakeSize = 0
    private var realSize = 0
    private var currentPosition = 0
    private var itemList: List<com.breckworld.model.mds.Slide>? = null

    var layoutManager: LinearLayoutManager? = null
    var position = 0
    var timer: Timer? = null
    var timerTask: TimerTask? = null
    var bannerListSize: Int? = null

    companion object {
        val RESULT_CODE = 100
        val VISIT_DATA = "visistData"
    }

    private val pageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position
                //onFocusChange()
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    try {
                        if (currentPosition === 0) {
                            visitBinding.viewPagerVideos.setCurrentItem(fakeSize - 2, false)
                        } else if (currentPosition === fakeSize - 1) {
                            visitBinding.viewPagerVideos.setCurrentItem(1, false)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else if (state == ViewPager2.SCROLL_STATE_DRAGGING && currentPosition === fakeSize) {
                    //we scroll too fast and miss the state SCROLL_STATE_IDLE for the previous item
                    visitBinding.viewPagerVideos.setCurrentItem(2, false)
                }
            }
        }

    /**
     * add two lasts item at beginning on the new array
     * @param itemList
     * @return
     */
    fun transformListAndAddTwo(itemList: List<com.breckworld.model.mds.Slide>): java.util.ArrayList<com.breckworld.model.mds.Slide> {
        val size: Int = itemList.size
        val listTemp: java.util.ArrayList<com.breckworld.model.mds.Slide> =
            java.util.ArrayList(size + 2)
        for (iPL in 0..size + 2) {
            listTemp.add(itemList[(iPL + size - 2) % size])
        }
        return listTemp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        visitBinding = ActivityVisitBinding.inflate(layoutInflater)
        setContentView(visitBinding.root)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        apiService = ApiClient.createService(ApiInterface::class.java, this)
        val repositories = Repository2(apiService!!, this)

        visitVM = ViewModelProvider(
            this, VisitVMFactory(repositories)
        ).get(VisitVM::class.java)

        if (intent != null) {
            cameFrom = intent.getStringExtra("cameFrom").toString()
            linkedMdsId = intent.getIntExtra("linkedMdsId", 0)
            slideId = intent.getIntExtra("slideId", 0)
        }

        /**
         * Call mds api
         */
        if (slideId == 0) {
            visitVM.getMdsApiData(linkedMdsId.toString(), "", App.mLocalStore?.accessToken)
        }
        else {
            visitVM.getMdsApiData(linkedMdsId.toString(), slideId.toString(), App.mLocalStore?.accessToken)
        }

        visitVM.mdsLiveData.observe(this, Observer {
            when (it) {
                is ResponseListener.Success -> {
                    if (it.data != null) {

                        visitAdapter = VisitAdapter(
                            this@VisitActivity,
                            this@VisitActivity,
                            it.data.data.banners,
                            it.data.data.buttons,
                            it.data.data.slides,
                            it.data.data.statuses,
                            it.data.data.footer,
                            it.data.data.ctabs,
                            cameFrom
                        )
                        visitBinding.viewPagerVideos.setOrientation(ViewPager2.ORIENTATION_VERTICAL)
                        visitBinding.viewPagerVideos.setAdapter(visitAdapter)
                        visitAdapter?.setOnClickListener(object :
                            VisitAdapter.OnClickListener {
                            override fun onShowThisDealClickListener(linked_mds_id: Int) {
                                if (linked_mds_id == -1) {
                                    /*visitVM.getMdsApiData(
                                        linkedMdsId.toString(),
                                        App.mLocalStore?.accessToken
                                    )*/
                                    finish()
                                } else {
                                    visitVM.getMdsApiData(
                                        linked_mds_id.toString(),
                                        "",
                                        App.mLocalStore?.accessToken
                                    )
                                }
                            }

                            override fun onMdsBottomButtonClickListener(position: Int) {

                            }

                            override fun onCloseClickListener(position: Int) {
                                finish()
                            }
                        })

                        // Set Header & Footer
                        setDataForHeaderFooter(it.data.data.slides, it.data.data.statuses, it.data.data.footer)


                        // Set Data for Banners
                        if (it.data.data.banners != null && it.data.data.banners.size > 0) {
                            visitBinding.recyclerViewBanners.visibility = View.VISIBLE
                            setDataToAdapterForBanners(it.data.data.banners)
                        } else {
                            visitBinding.recyclerViewBanners.visibility = View.GONE
                        }

                        // Set Right Buttons
                        mdsButtonsAdapter = MdsButtonsAdapter(this, it.data.data.buttons)
                        visitBinding.recyclerViewMDSButtons.layoutManager =
                            LinearLayoutManager(this)
                        visitBinding.recyclerViewMDSButtons.adapter = mdsButtonsAdapter
                        mdsButtonsAdapter?.setOnClickListener(object :
                            MdsButtonsAdapter.OnClickListener {
                            override fun onButtonClickListener(position: Int) {
                                buttonClickedPosition = position
                                var pos = fromPosition
                                if (it.data.data.slides.size > 2) {
                                    if (fromPosition == 1)
                                        pos = 2
                                    else if (fromPosition == 2) {
                                        pos = 0
                                    } else
                                        pos = fromPosition - 2
                                } else if (it.data.data.slides.size == 2) {
                                    if (fromPosition == 1) {
                                        pos = 1
                                    } else {
                                        pos = 0
                                    }
                                } else {
                                    pos = fromPosition
                                }

                                if (it.data.data.buttons.get(position).type.equals("INFO")) {

                                    val intent =
                                        Intent(this@VisitActivity, MDSInfoActivity::class.java)
                                    intent.putExtra("slides", it.data.data.slides.get(pos))
                                    intent.putExtra(
                                        "info",
                                        it.data.data.buttons.get(position).data.html
                                    )
                                    intent.putParcelableArrayListExtra(
                                        "footer",
                                        ArrayList(it.data.data.footer)
                                    )
                                    if (it.data.data.ctabs != null && it.data.data.ctabs.size > 0)
                                        intent.putExtra("ctabs", it.data.data.ctabs.get(pos))
                                    intent.putExtra("position", pos)
                                    startActivity(intent)

                                } else if (it.data.data.buttons.get(position).type.equals("FAVORITE")) {

                                    /**
                                     * Call fav ̣
                                     */
                                    visitVM.favApiCall(
                                        linkedMdsId.toString(),
                                        App.mLocalStore?.accessToken
                                    )

                                } else if(it.data.data.buttons.get(position).type.equals("QUICK_SLIDE")) {

                                    val intent = Intent(this@VisitActivity, QuickSlideActivity::class.java)
                                    intent.putExtra("cameFrom", cameFrom)
                                    intent.putExtra("bgImage", it.data.data.images.list_bg)
                                    intent.putParcelableArrayListExtra("slides", ArrayList(it.data.data.slides))
                                    intent.putParcelableArrayListExtra(
                                        "footer",
                                        ArrayList(it.data.data.footer)
                                    )
                                    if (it.data.data.ctabs != null && it.data.data.ctabs.size > 0)
                                        intent.putExtra("ctabs", it.data.data.ctabs.get(pos))
                                    intent.putExtra("position", pos)
                                    startActivityForResult(intent, 101)

                                } else if (it.data.data.buttons.get(position).type.equals("LEADERBOARD")) {

                                    startActivity(Intent(this@VisitActivity, LeaderboardActivity::class.java)
                                        .putExtra("slides", it.data.data.slides.get(pos))
                                        .putExtra("me", it.data.data.buttons.get(position).data.leaders.me)
                                        .putParcelableArrayListExtra("names", ArrayList(it.data.data.buttons.get(position).data.leaders.all))
                                        .putParcelableArrayListExtra("footer", ArrayList(it.data.data.footer)))

                                } else {
                                    // For other types of buttons
                                }
                            }
                        })

                        if (it.data.data.slides.size > 1) {
                            visitBinding.viewPagerVideos.registerOnPageChangeCallback(
                                pageChangeCallback
                            )
                            realSize = it.data.data.slides.size
                            fakeSize = realSize + 2;
                            itemList = transformListAndAddTwo(it.data.data.slides)
                            visitAdapter?.updateList(itemList!!)
                            var item =
                                2 //number two because we have two elements before the chosen one
                            visitBinding.viewPagerVideos.setCurrentItem(item, false)
                        }

                        for (i in it.data.data.slides.indices) {
                            if(it.data.data.ids.active_slide == it.data.data.slides.get(i).ids.slide) {
                                visitBinding.viewPagerVideos.setCurrentItem(i, false)
                            }
                        }
                    }
                }
                is ResponseListener.Failure -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                is ResponseListener.Error -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })

        visitBinding.viewPagerVideos.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                fromPosition = position
                Log.d("fromPosition : ", "" + fromPosition)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    visitAdapter?.setFadeIn(true, fromPosition)
                    //visitAdapter?.notifyItemChanged(fromPosition)
                } else {
                    visitAdapter?.setFadeIn(false, fromPosition)
                    //visitAdapter?.notifyItemChanged(fromPosition)
                }
            }
        })

        visitVM.favLiveData.observe(this, Observer {
            when (it) {
                is ResponseListener.Success -> {
                    if (it.data != null) {
                        try {
                            if (it.data.message.equals("Favorite Added")) {
                                mdsButtonsAdapter?.updateFavIcon(true, buttonClickedPosition)
                            } else {
                                mdsButtonsAdapter?.updateFavIcon(false, buttonClickedPosition)
                            }
                        } catch(e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                is ResponseListener.Failure -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                is ResponseListener.Error -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun setDataForHeaderFooter(
        slides: List<Slide>,
        statuses: List<String>,
        footer: List<Footer>
    ) {
        try {
            Picasso.get().load(slides.get(position).images.logo)
                .into(visitBinding.imageViewLogo)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        visitBinding.textViewCompanyTitle.text = slides.get(position).labels.header
        visitBinding.textViewCompanySubTitle.text = slides.get(position).labels.subheader

        visitBinding.imageViewForHeaderAlpha.setBackgroundColor(
            Color.parseColor(
                slides.get(
                    position
                ).colors.header_bg
            )
        )
        window.statusBarColor = Color.parseColor(
            slides.get(
                position
            ).colors.header_bg
        )

        visitBinding.imageViewForHeaderAlpha.alpha =
            slides.get(position).colors.header_opacity.toFloat()

        visitBinding.imageViewForBottomAlpha.setBackgroundColor(
            Color.parseColor(
                slides.get(
                    position
                ).colors.footer_bg
            )
        )
        visitBinding.imageViewForBottomAlpha.alpha =
            slides.get(position).colors.footer_opacity.toFloat()

        window.navigationBarColor = Color.parseColor(
            slides.get(
                position
            ).colors.footer_bg
        )

        visitBinding.textViewCompanyTitle.setTextColor(Color.parseColor(slides.get(position).colors.header_text))


        //visitBinding.textViewOpenTime.text = HtmlCompat.fromHtml("<b>Open</b>" + ": Now till 23:00", HtmlCompat.FROM_HTML_MODE_LEGACY)
        visitBinding.textViewOpenTime.text = HtmlCompat.fromHtml(
            "<b>${slides.get(position).labels.footer}</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        visitBinding.textViewOpenTime.setTextColor(Color.parseColor(slides.get(position).colors.footer_text))
        visitBinding.textViewCompanySubTitle.setTextColor(Color.parseColor(slides.get(position).colors.footer_text))

        for (i in statuses) {
            if (i.equals("CAN_CLOSE")) {
                visitBinding.textViewClose.visibility = View.VISIBLE
                break
            } else {
                visitBinding.textViewClose.visibility = View.GONE
            }

            if (i.equals("CAN_BACK")) {
                visitBinding.imageViewBack.visibility = View.VISIBLE
                break
            } else {
                visitBinding.imageViewBack.visibility = View.GONE
            }
        }

        visitBinding.textViewClose.setOnClickListener {
            finish()
        }

        visitBinding.imageViewBack.setOnClickListener {
            finish()
        }

        // Set bottom Buttons
        val mdsBottomIconsAdapter =
            MdsBottomIconsAdapter(this, footer, slides.get(position).colors)
        visitBinding.recyclerViewBottomIcons.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        visitBinding.recyclerViewBottomIcons.adapter = mdsBottomIconsAdapter
        mdsBottomIconsAdapter.setOnClickListener(object :
            MdsBottomIconsAdapter.OnClickListener {
            override fun onIconClickListener(position: Int) {
                if (footer.get(position).type.equals("PHONE")) {
                    mobileNo = footer.get(position).data.phone
                    checkPermission()
                } else {
                    if (footer.get(position).data.url != null && !footer.get(
                            position
                        ).data.url.equals("")
                    ) {
                        val intent =
                            Intent(this@VisitActivity, WebViewActivity::class.java)
                        intent.putExtra(
                            "title",
                            footer.get(position).labels.title
                        )
                        intent.putExtra(
                            "url",
                            footer.get(position).data.url
                        )
                        startActivity(intent)
                    }
                }
            }
        })
    }

    private fun setDataToAdapterForBanners(bannerList: List<Banner>) {
        val visitBannersAdapter = VisitBannersAdapter(this@VisitActivity, bannerList)
        layoutManager = LinearLayoutManager(this@VisitActivity)
        visitBinding.recyclerViewBanners.layoutManager = layoutManager
        visitBinding.recyclerViewBanners.adapter = visitBannersAdapter
        visitBannersAdapter.setOnClickListener(object :
            VisitBannersAdapter.OnClickListener {
            override fun onBannerClickListener(position: Int) {
                startActivity(
                    Intent(this@VisitActivity, VisitActivity::class.java)
                        .putExtra("cameFrom", cameFrom)
                        .putExtra(
                            "linkedMdsId",
                            bannerList.get(position).target.mds_id
                        )
                )
            }
        })

        // Set banner scrolling
        bannerListSize = bannerList.size
        position = 0
        visitBinding.recyclerViewBanners.scrollToPosition(position)

        val snapHelper: SnapHelper = LinearSnapHelper()
        try {
            snapHelper.attachToRecyclerView(visitBinding.recyclerViewBanners)
            visitBinding.recyclerViewBanners.smoothScrollBy(5, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        visitBinding.recyclerViewBanners.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                @NonNull recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 1) {
                    //stopAutoScrollBanner()
                } else if (newState == 0) {
                    //position = layoutManager!!.findFirstCompletelyVisibleItemPosition()
                    if (bannerListSize!! > 0)
                        runAutoScrollBanner()
                }
            }
        })
    }

    private fun stopAutoScrollBanner() {
        if (timer != null && timerTask != null) {
            timerTask?.cancel()
            timer?.cancel()
            timer = null
            timerTask = null
            try {
                position = layoutManager!!.findFirstCompletelyVisibleItemPosition()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun runAutoScrollBanner() {
        if (timer == null && timerTask == null) {
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    if (position == Int.MAX_VALUE) {
                        position = Int.MAX_VALUE / 2
                        visitBinding.recyclerViewBanners.scrollToPosition(position)
                        visitBinding.recyclerViewBanners.smoothScrollBy(0, 5)
                    } else {
                        position++
                        visitBinding.recyclerViewBanners.smoothScrollToPosition(position)

                        if (position == bannerListSize) {
                            visitBinding.recyclerViewBanners.smoothScrollToPosition(0)
                            position = 0
                        }
                    }
                }
            }
            timer?.schedule(timerTask, 5000, 5000)
        }
    }

    override fun onResume() {
        super.onResume()
        runAutoScrollBanner()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScrollBanner()
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CALL_PHONE
                )
            ) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42
                )
            }
        } else {
            // Permission has already been granted
            callPhone()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                callPhone()
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }

    fun callPhone() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNo))
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_CODE) {
            val testResult = data?.getIntExtra(VISIT_DATA, 0)
            for (i in itemList?.indices!!) {
                if(testResult == itemList?.get(i)?.ids?.slide) {
                    visitBinding.viewPagerVideos.setCurrentItem(i, false)
                }
            }
        }
    }
}