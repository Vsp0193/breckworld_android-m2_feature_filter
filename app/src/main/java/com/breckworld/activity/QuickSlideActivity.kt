package com.breckworld.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.adapter.MdsBottomIconsAdapter
import com.breckworld.adapter.QuickSlideAdapter
import com.breckworld.databinding.ActivityQuickSlideBinding
import com.breckworld.fragment.GeoStoreFragment
import com.breckworld.model.mds.Ctabs
import com.breckworld.model.mds.Footer
import com.breckworld.model.mds.Slide
import com.breckworld.util.AppUtil
import com.bumptech.glide.Glide

class QuickSlideActivity : AppCompatActivity() {

    lateinit var quickSlideBinding: ActivityQuickSlideBinding
    private var slideList: List<Slide>?= null
    private var footerList: List<Footer>?= null
    private var ctab: Ctabs? = null
    private var position: Int = 0
    private var mobileNo: String = ""
    private var cameFrom: String = ""
    private var bgImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quickSlideBinding = ActivityQuickSlideBinding.inflate(layoutInflater)
        setContentView(quickSlideBinding.root)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (intent != null) {
            cameFrom = intent.getStringExtra("cameFrom").toString()
            bgImage = intent.getStringExtra("bgImage").toString()
            slideList = intent.getParcelableArrayListExtra("slides")
            footerList = intent.getParcelableArrayListExtra("footer")
            ctab = intent.getParcelableExtra("ctabs")
            position = intent.getIntExtra("position", 0)
        }

        setData()
        setMainContent()
        setBottomButtons()

        quickSlideBinding.imageViewBack.setOnClickListener {
            finish()
        }

        quickSlideBinding.textViewClose.setOnClickListener {
            if (cameFrom.equals("Home")) {
                startActivity(Intent(this, HomeActivity::class.java)
                    .putExtra("cameFrom", cameFrom))
            } else if (cameFrom.equals("GeoStore")) {
                startActivity(Intent(this, HomeActivity::class.java)
                    .putExtra("cameFrom", cameFrom))
            } else if (cameFrom.equals("Fav")) {
                startActivity(Intent(this, HomeActivity::class.java)
                    .putExtra("cameFrom", cameFrom))
            } else if (cameFrom.equals("History")) {
                startActivity(Intent(this, HomeActivity::class.java)
                    .putExtra("cameFrom", cameFrom))
            } else if (cameFrom.equals("Wallet")) {
                startActivity(Intent(this, HomeActivity::class.java)
                    .putExtra("cameFrom", cameFrom))
            } else {
                startActivity(Intent(this, HomeActivity::class.java)
                    .putExtra("cameFrom", "Home"))
            }
            finishAffinity()
        }
    }


    private fun setData() {

        if(!bgImage.equals("")) {
            Glide.with(this).load(bgImage).into(quickSlideBinding.imageViewBg)
        } else {
            quickSlideBinding.imageViewBg.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.filter_bg))
        }

        try {
            Glide.with(this).load(slideList?.get(position)?.images?.logo)
                .into(quickSlideBinding.imageViewLogo)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /*Glide.with(this).load(slideList?.get(position)?.images?.slide_full)
            .into(quickSlideBinding.imageViewImage)*/
        try {
            quickSlideBinding.textViewCompanyTitle.text = slideList?.get(position)?.labels?.header
            quickSlideBinding.textViewCompanySubTitle.text =
                slideList?.get(position)?.labels?.subheader

            quickSlideBinding.imageViewForHeaderAlpha.setBackgroundColor(
                Color.parseColor(
                    slideList?.get(position)?.colors?.header_bg
                )
            )
            window.statusBarColor = Color.parseColor(
                slideList?.get(position)?.colors?.header_bg
            )

            quickSlideBinding.imageViewForHeaderAlpha.alpha =
                slideList?.get(position)?.colors?.header_opacity?.toFloat()!!

            quickSlideBinding.imageViewForBottomAlpha.setBackgroundColor(
                Color.parseColor(
                    slideList?.get(position)?.colors?.footer_bg
                )
            )
            quickSlideBinding.imageViewForBottomAlpha.alpha =
                slideList?.get(position)?.colors?.footer_opacity?.toFloat()!!

            window.navigationBarColor = Color.parseColor(
                slideList?.get(position)?.colors?.footer_bg
            )

            quickSlideBinding.textViewOpenTime.text = HtmlCompat.fromHtml(
                "<b>${slideList?.get(position)?.labels?.footer}</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            quickSlideBinding.textViewOpenTime.setTextColor(Color.parseColor(slideList?.get(position)?.colors?.footer_text))
            quickSlideBinding.textViewCompanySubTitle.setTextColor(
                Color.parseColor(
                    slideList?.get(
                        position
                    )?.colors?.footer_text
                )
            )
        } catch (e: Exception) { e.printStackTrace() }

        if (ctab != null) {
            quickSlideBinding.layoutCtab.visibility = View.VISIBLE
            quickSlideBinding.layoutCtab.setBackgroundTintList(
                ColorStateList.valueOf(
                    Color.parseColor(
                        ctab?.colors?.banner_bg
                    )
                )
            )
            quickSlideBinding.textViewCtab.setTextColor(Color.parseColor(ctab?.colors?.banner_text))
            quickSlideBinding.textViewCtab.text =ctab?.label
            quickSlideBinding.imageViewCtab.setImageDrawable(
                AppUtil.findImageResource(
                    this,
                    ctab?.icon!!
                )
            )
        } else {
            quickSlideBinding.layoutCtab.visibility = View.GONE
        }
    }

    private fun setMainContent() {
        var quickSlideAdapter = QuickSlideAdapter(this, slideList!!)
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        quickSlideBinding.recyclerViewQuickSlide.setLayoutManager(mLayoutManager)
        quickSlideBinding.recyclerViewQuickSlide.adapter = quickSlideAdapter
        quickSlideAdapter.setOnClickListener(object:
        QuickSlideAdapter.OnClickListener {
            override fun onCategoryClickListener(position: Int) {
                val intent = Intent()
                intent.putExtra(
                    VisitActivity.VISIT_DATA,
                    slideList?.get(position)?.ids?.slide
                )
                setResult(
                    VisitActivity.RESULT_CODE,
                    intent
                )
                finish()
            }
        })
    }

    private fun setBottomButtons() {
        // Set bottom Buttons
        val mdsBottomIconsAdapter =
            MdsBottomIconsAdapter(this, footerList!!, slideList?.get(position)?.colors!!)
        quickSlideBinding.recyclerViewBottomIcons.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        quickSlideBinding.recyclerViewBottomIcons.adapter = mdsBottomIconsAdapter
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
                            Intent(this@QuickSlideActivity, WebViewActivity::class.java)
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