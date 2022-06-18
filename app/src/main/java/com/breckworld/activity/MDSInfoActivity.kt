package com.breckworld.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.adapter.GeoStoreAdapter
import com.breckworld.adapter.MdsBottomIconsAdapter
import com.breckworld.databinding.ActivityMdsinfoBinding
import com.breckworld.databinding.ActivityVisitBinding
import com.breckworld.model.mds.Ctabs
import com.breckworld.model.mds.Footer
import com.breckworld.model.mds.Slide
import com.breckworld.util.AppUtil
import com.bumptech.glide.Glide

class MDSInfoActivity : AppCompatActivity() {

    lateinit var mdsInfoBinding: ActivityMdsinfoBinding
    private var slide: Slide? = null
    private var footerList: List<Footer>?= null
    private var ctab: Ctabs? = null
    private var position: Int = 0
    private var infoText: String = ""
    private var mobileNo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mdsInfoBinding = ActivityMdsinfoBinding.inflate(layoutInflater)
        setContentView(mdsInfoBinding.root)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (intent != null) {
            slide = intent.getParcelableExtra("slides")
            footerList = intent.getParcelableArrayListExtra("footer")
            ctab = intent.getParcelableExtra("ctabs")
            position = intent.getIntExtra("position", 0)
            infoText = intent.getStringExtra("info").toString()
        }

        try {
            Glide.with(this).load(slide?.images?.logo)
                .into(mdsInfoBinding.imageViewLogo)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Glide.with(this).load(slide?.images?.slide_full)
            .into(mdsInfoBinding.imageViewImage)
        mdsInfoBinding.textViewCompanyTitle.text = slide?.labels?.header
        mdsInfoBinding.textViewCompanySubTitle.text = slide?.labels?.subheader

        mdsInfoBinding.textViewInfo.text = HtmlCompat.fromHtml(
            infoText,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        mdsInfoBinding.imageViewForHeaderAlpha.setBackgroundColor(
            Color.parseColor(
                slide?.colors?.header_bg
            )
        )
        window.statusBarColor = Color.parseColor(
            slide?.colors?.header_bg
        )

        mdsInfoBinding.imageViewForHeaderAlpha.alpha =
            slide?.colors?.header_opacity?.toFloat()!!

        mdsInfoBinding.imageViewForBottomAlpha.setBackgroundColor(
            Color.parseColor(
                slide?.colors?.footer_bg
            )
        )
        mdsInfoBinding.imageViewForBottomAlpha.alpha =
            slide?.colors?.footer_opacity?.toFloat()!!

        window.navigationBarColor = Color.parseColor(
            slide?.colors?.footer_bg
        )

        mdsInfoBinding.textViewOpenTime.text = HtmlCompat.fromHtml(
            "<b>${slide?.labels?.footer}</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        mdsInfoBinding.textViewOpenTime.setTextColor(Color.parseColor(slide?.colors?.footer_text))
        mdsInfoBinding.textViewCompanySubTitle.setTextColor(Color.parseColor(slide?.colors?.footer_text))

        if (ctab != null) {
            mdsInfoBinding.layoutCtab.visibility = View.VISIBLE
            mdsInfoBinding.layoutCtab.setBackgroundTintList(
                ColorStateList.valueOf(
                    Color.parseColor(
                       ctab?.colors?.banner_bg
                    )
                )
            )
            mdsInfoBinding.textViewCtab.setTextColor(Color.parseColor(ctab?.colors?.banner_text))
            mdsInfoBinding.textViewCtab.text =ctab?.label
            mdsInfoBinding.imageViewCtab.setImageDrawable(
                AppUtil.findImageResource(
                    this,
                   ctab?.icon!!
                )
            )
        } else {
            mdsInfoBinding.layoutCtab.visibility = View.GONE
        }

        setBottomButtons()

        mdsInfoBinding.imageViewBack.setOnClickListener {
            finish()
        }
    }

    private fun setBottomButtons() {
        // Set bottom Buttons
        val mdsBottomIconsAdapter =
            MdsBottomIconsAdapter(this, footerList!!, slide?.colors!!)
        mdsInfoBinding.recyclerViewBottomIcons.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mdsInfoBinding.recyclerViewBottomIcons.adapter = mdsBottomIconsAdapter
        mdsBottomIconsAdapter.setOnClickListener(object :
            MdsBottomIconsAdapter.OnClickListener {
            override fun onIconClickListener(position: Int) {
                if (footerList?.get(position)?.type.equals("PHONE")) {
                    mobileNo = footerList?.get(position)?.data?.phone.toString()
                    checkPermission()
                } else {
                    if (footerList?.get(position)?.data?.url != null && !footerList?.get(
                            position
                        )?.data?.url.equals("")
                    ) {
                        val intent =
                            Intent(this@MDSInfoActivity, WebViewActivity::class.java)
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