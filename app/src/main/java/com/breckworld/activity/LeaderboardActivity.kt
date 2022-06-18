package com.breckworld.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.breckworld.adapter.LeaderboardAdapter
import com.breckworld.adapter.MdsBottomIconsAdapter
import com.breckworld.databinding.ActivityLeaderboardBinding
import com.breckworld.model.mds.All
import com.breckworld.model.mds.Footer
import com.breckworld.model.mds.Me
import com.breckworld.model.mds.Slide
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso


class LeaderboardActivity : AppCompatActivity() {

    private lateinit var leaderboardBinding: ActivityLeaderboardBinding
    private var slide: Slide? = null
    private var me: Me? = null
    private var namesList: ArrayList<All>? = null
    private var footerList: ArrayList<Footer>?= null
    private var position: Int = 0
    private var mobileNo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leaderboardBinding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(leaderboardBinding.root)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (intent != null) {
            slide = intent.getParcelableExtra("slides")
            me = intent.getParcelableExtra("me")
            namesList = intent.getParcelableArrayListExtra("names")
            footerList = intent.getParcelableArrayListExtra("footer")
            position = intent.getIntExtra("position", 0)
        }

        setData()
        setDataForAdapter()

        leaderboardBinding.imageViewBack.setOnClickListener {
            finish()
        }

    }

    private fun setData() {
        try {
            Picasso.get().load(slide?.images?.logo)
                .into(leaderboardBinding.imageViewLogo)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Glide.with(this).load(slide?.images?.slide_full)
            .into(leaderboardBinding.imageViewImage)
        leaderboardBinding.textViewCompanyTitle.text = slide?.labels?.header
        leaderboardBinding.textViewCompanyTitle.setTextColor(Color.parseColor(slide?.colors?.header_text))

        leaderboardBinding.textViewOpenTime.text = HtmlCompat.fromHtml(
            "<b>${slide?.labels?.footer}</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        leaderboardBinding.textViewOpenTime.setTextColor(Color.parseColor(slide?.colors?.footer_text))
        leaderboardBinding.textViewCompanySubTitle.setTextColor(Color.parseColor(slide?.colors?.footer_text))

        leaderboardBinding.imageViewForHeaderAlpha.setBackgroundColor(
            Color.parseColor(
                slide?.colors?.header_bg
            )
        )
        window.statusBarColor = Color.parseColor(
            slide?.colors?.header_bg
        )

        leaderboardBinding.imageViewForHeaderAlpha.alpha =
            slide?.colors?.header_opacity?.toFloat()!!

        leaderboardBinding.imageViewForBottomAlpha.setBackgroundColor(
            Color.parseColor(
                slide?.colors?.footer_bg
            )
        )
        leaderboardBinding.imageViewForBottomAlpha.alpha =
            slide?.colors?.footer_opacity?.toFloat()!!

        window.navigationBarColor = Color.parseColor(
            slide?.colors?.footer_bg
        )

        // Set bottom Buttons
        val mdsBottomIconsAdapter =
            MdsBottomIconsAdapter(this@LeaderboardActivity, footerList!!, slide?.colors!!)
        leaderboardBinding.recyclerViewBottomIcons.layoutManager =
            LinearLayoutManager(this@LeaderboardActivity, LinearLayoutManager.HORIZONTAL, false)
        leaderboardBinding.recyclerViewBottomIcons.adapter = mdsBottomIconsAdapter
        mdsBottomIconsAdapter.setOnClickListener(object :
            MdsBottomIconsAdapter.OnClickListener {
            override fun onIconClickListener(position: Int) {
                if (footerList?.get(position)?.type.equals("PHONE")) {
                    mobileNo = footerList?.get(position)?.data?.phone.toString()
                    checkPermission()
                } else {
                    if (footerList?.get(position)?.data?.url != null && !footerList?.get(position)?.data?.url.equals("")
                    ) {
                        val intent =
                            Intent(this@LeaderboardActivity, WebViewActivity::class.java)
                        intent.putExtra(
                            "title",
                            footerList?.get(position)?.labels?.title
                        )
                        intent.putExtra(
                            "url",
                            footerList?.get(position)?.data?.url
                        )
                        startActivity(intent)
                    }
                }
            }
        })
    }

    private fun setDataForAdapter() {
        val leaderBoardAdapter = LeaderboardAdapter(this, namesList!!, me)
        leaderboardBinding.recyclerViewLeaderboard.layoutManager =
            LinearLayoutManager(this)
        leaderboardBinding.recyclerViewLeaderboard.adapter = leaderBoardAdapter

        val linearSmoothScroller: LinearSmoothScroller =
            object : LinearSmoothScroller(leaderboardBinding.recyclerViewLeaderboard.getContext()) {
                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return 5.0f / displayMetrics.densityDpi
                }
            }

        if(namesList?.size!! >= 1000) {
            linearSmoothScroller.targetPosition = leaderBoardAdapter.getItemCount() - 1
        } else {
            if (me?.position!! + 8 >= 1000) {
                linearSmoothScroller.targetPosition = me?.position!! + 8
            } else {
                linearSmoothScroller.targetPosition = me?.position!!
            }
        }

        (leaderboardBinding.recyclerViewLeaderboard.layoutManager as LinearLayoutManager).startSmoothScroll(linearSmoothScroller)

        /*leaderboardBinding.recyclerViewLeaderboard.post(Runnable { // Call smooth scroll
            if(namesList?.size!! >= 1000) {
                leaderboardBinding.recyclerViewLeaderboard.smoothScrollToPosition(leaderBoardAdapter.getItemCount() - 1)
            } else {
                if (me?.position!! + 8 >= 1000)
                    leaderboardBinding.recyclerViewLeaderboard.smoothScrollToPosition(me?.position!! + 8)
                else
                    leaderboardBinding.recyclerViewLeaderboard.smoothScrollToPosition(me?.position!!)
            }
        })*/
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

}