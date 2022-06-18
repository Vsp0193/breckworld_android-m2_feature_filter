package com.breckworld.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.breckworld.R
import com.breckworld.activity.VisitActivity
import com.breckworld.model.mds.*
import com.breckworld.model.mds.Button
import com.breckworld.util.AppUtil
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import java.util.*


class VisitAdapter(
    private var context: Context,
    private var activity: Activity,
    private var bannerList: List<Banner>,
    private var buttonList: List<Button>,
    private var slidesList: List<Slide>,
    private var statusesList: List<String>,
    private var footerList: List<Footer>,
    private var ctabsList: List<Ctabs>,
    private var cameFrom: String,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var canBack: Boolean? = false
    private var shouldFadeIn: Boolean = false


    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_visit, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)

        /*try {
            *//*Glide.with(context).load(slidesList.get(position).images.logo)
                .into(holderType.imageViewLogo)*//*
            Picasso.get().load(slidesList.get(position).images.logo)
                .into(holderType.imageViewLogo)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/

        Glide.with(context).load(slidesList.get(position).images.slide_full)
            .into(holderType.imageViewImage)
        //holderType.textViewCompanyTitle.text = slidesList.get(position).labels.header
        //holderType.textViewCompanySubTitle.text = slidesList.get(position).labels.subheader
        holderType.txtTitle.text = slidesList.get(position).labels.title
        holderType.txtDesc.visibility = View.INVISIBLE
        holderType.txtDesc.maxLines = 3
        holderType.txtDesc.text = slidesList.get(position).descriptions.long

        if (shouldFadeIn) {
            holderType.layoutBottomLeft.alpha = 0.5f
        } else {
            holderType.layoutBottomLeft.alpha = 1f
        }

        holderType.txtDesc.post(Runnable {
            var lineCount = holderType.txtDesc.getLineCount()

            if (lineCount > 3) {
                var trimmedText = ""
                holderType.txtDesc.post(Runnable {
                    trimmedText = AppUtil.getVisibleText(holderType.txtDesc).toString()
                    //trimmedText.dropLast(13)
                    holderType.txtDesc.text = HtmlCompat.fromHtml(
                        AppUtil.removeChars(trimmedText, 10)
                            ?.substringBeforeLast(" ") + "<b> ... more</b>",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                })
                holderType.txtDesc.visibility = View.VISIBLE

            } else {
                holderType.txtDesc.maxLines = 10
                holderType.txtDesc.text = slidesList.get(position).descriptions.long
                holderType.txtDesc.visibility = View.VISIBLE
            }
        })

        //holderType.layoutHeader.setBackgroundColor(Color.parseColor(slidesList.get(position).colors.header_bg))
        /*holderType.imageViewForHeaderAlpha.setBackgroundColor(
            Color.parseColor(
                slidesList.get(
                    position
                ).colors.header_bg
            )
        )
        activity.window.statusBarColor = Color.parseColor(
            slidesList.get(
                position
            ).colors.header_bg
        )

        holderType.imageViewForHeaderAlpha.alpha =
            slidesList.get(position).colors.header_opacity.toFloat()

        holderType.imageViewForBottomAlpha.setBackgroundColor(
            Color.parseColor(
                slidesList.get(
                    position
                ).colors.footer_bg
            )
        )
        holderType.imageViewForBottomAlpha.alpha =
            slidesList.get(position).colors.footer_opacity.toFloat()

        activity.window.navigationBarColor = Color.parseColor(
            slidesList.get(
                position
            ).colors.footer_bg
        )

        holderType.textViewCompanyTitle.setTextColor(Color.parseColor(slidesList.get(position).colors.header_text))


        //holderType.textViewOpenTime.text = HtmlCompat.fromHtml("<b>Open</b>" + ": Now till 23:00", HtmlCompat.FROM_HTML_MODE_LEGACY)
        holderType.textViewOpenTime.text = HtmlCompat.fromHtml(
            "<b>${slidesList.get(position).labels.footer}</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        holderType.textViewOpenTime.setTextColor(Color.parseColor(slidesList.get(position).colors.footer_text))
        holderType.textViewCompanySubTitle.setTextColor(Color.parseColor(slidesList.get(position).colors.footer_text))

        if (slidesList.get(position).ids.linked_mds != null) {
            holderType.layoutShowThisDeal.visibility = View.VISIBLE
        } else {
            holderType.layoutShowThisDeal.visibility = View.INVISIBLE
        }

        for (i in statusesList) {
            if (i.equals("CAN_CLOSE")) {
                holderType.textViewClose.visibility = View.VISIBLE
                break
            } else {
                holderType.textViewClose.visibility = View.GONE
            }

            if (i.equals("CAN_BACK")) {
                canBack = true
                holderType.imageViewBack.visibility = View.VISIBLE
                break
            } else {
                canBack = false
                holderType.imageViewBack.visibility = View.GONE
            }
        }*/

        if (ctabsList != null && ctabsList.size > 0) {
            holderType.layoutCtab.visibility = View.VISIBLE
            holderType.layoutCtab.setBackgroundTintList(
                ColorStateList.valueOf(
                    Color.parseColor(
                        ctabsList.get(0).colors.banner_bg
                    )
                )
            )
            holderType.textViewCtab.setTextColor(Color.parseColor(ctabsList.get(0).colors.banner_text))
            holderType.textViewCtab.text = ctabsList.get(0).label
            holderType.imageViewCtab.setImageDrawable(
                AppUtil.findImageResource(
                    context,
                    ctabsList.get(0).icon
                )
            )
        } else {
            holderType.layoutCtab.visibility = View.GONE
        }

        holderType.layoutShowThisDeal.setOnClickListener {
            onClickListener?.onShowThisDealClickListener(slidesList.get(position).ids.linked_mds!!.toInt())
        }

        /*holderType.textViewClose.setOnClickListener {
            onClickListener?.onCloseClickListener(position)
        }*/

        /*holderType.imageViewBack.setOnClickListener {
            if (holderType.layoutCtab.visibility == View.VISIBLE) {
                holderType.textViewClose.visibility = View.GONE
                if (!canBack!!)
                    holderType.imageViewBack.visibility = View.GONE
                onClickListener?.onShowThisDealClickListener(-1)

            } else {
                holderType.textViewClose.visibility = View.VISIBLE
                holderType.imageViewBack.visibility = View.GONE
            }
        }*/

        holderType.txtDesc.setOnClickListener {
            var moreWord =
                holderType.txtDesc.text.substring(holderType.txtDesc.text.lastIndexOf(" ") + 1)
            if (moreWord.contains("more")) {
                holderType.txtDesc.maxLines = 10
                holderType.txtDesc.text = HtmlCompat.fromHtml(
                    slidesList.get(position).descriptions.long + "   <b>less</b>",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            } else {
                holderType.txtDesc.post(Runnable {
                    var lineCount = holderType.txtDesc.getLineCount()

                    if (lineCount > 3) {
                        holderType.txtDesc.maxLines = 3
                        var trimmedText = ""
                        holderType.txtDesc.post(Runnable {
                            trimmedText = AppUtil.getVisibleText(holderType.txtDesc).toString()
                            holderType.txtDesc.text = HtmlCompat.fromHtml(
                                AppUtil.removeChars(trimmedText, 10)
                                    ?.substringBeforeLast(" ") + "<b> ... more</b>",
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
                        })
                    } else {
                        holderType.txtDesc.maxLines = 10
                        holderType.txtDesc.text = slidesList.get(position).descriptions.long
                    }
                })
            }
        }

        /*// Set bottom Buttons
        val mdsBottomIconsAdapter =
            MdsBottomIconsAdapter(context, footerList, slidesList.get(position).colors)
        holderType.recyclerViewBottomIcons.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holderType.recyclerViewBottomIcons.adapter = mdsBottomIconsAdapter
        mdsBottomIconsAdapter.setOnClickListener(object :
            MdsBottomIconsAdapter.OnClickListener {
            override fun onIconClickListener(position: Int) {
                onClickListener?.onMdsBottomButtonClickListener(position)
            }
        })*/

    }

    override fun getItemCount(): Int {
        return slidesList.size
    }

    inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
        var mVideoView: VideoView = itemView.findViewById(R.id.videoView)
        var imageViewImage: ImageView = itemView.findViewById(R.id.imageViewImage)
        var imageViewLogo: ImageView = itemView.findViewById(R.id.imageViewLogo)
        var textViewCompanyTitle: TextView = itemView.findViewById(R.id.textViewCompanyTitle)
        var textViewCompanySubTitle: TextView = itemView.findViewById(R.id.textViewCompanySubTitle)
        var txtTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var txtDesc: TextView = itemView.findViewById(R.id.textViewDes)
        var textViewOpenTime: TextView = itemView.findViewById(R.id.textViewOpenTime)
        var textViewClose: TextView = itemView.findViewById(R.id.textViewClose)
        var layoutBottom: RelativeLayout = itemView.findViewById(R.id.layoutBottom)
        var layoutHeader: RelativeLayout = itemView.findViewById(R.id.layoutHeader)
        var recyclerViewBottomIcons: RecyclerView =
            itemView.findViewById(R.id.recyclerViewBottomIcons)
        var layoutShowThisDeal: LinearLayout = itemView.findViewById(R.id.layoutShowThisDeal)
        var layoutMainContent: LinearLayout = itemView.findViewById(R.id.layoutMainContent)
        var imageViewBack: ImageView = itemView.findViewById(R.id.imageViewBack)
        var imageViewForHeaderAlpha: ImageView = itemView.findViewById(R.id.imageViewForHeaderAlpha)
        var imageViewForBottomAlpha: ImageView = itemView.findViewById(R.id.imageViewForBottomAlpha)
        var layoutCtab: RelativeLayout = itemView.findViewById(R.id.layoutCtab)
        var imageViewCtab: ImageView = itemView.findViewById(R.id.imageViewCtab)
        var textViewCtab: TextView = itemView.findViewById(R.id.textViewCtab)
        var imageViewShadow: ImageView = itemView.findViewById(R.id.imageViewShadow)
        var layoutBottomLeft: RelativeLayout = itemView.findViewById(R.id.layoutBottomLeft)
    }

    interface OnClickListener {
        fun onShowThisDealClickListener(linkedMdsId: Int)
        fun onMdsBottomButtonClickListener(position: Int)
        fun onCloseClickListener(position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setFadeIn(fadeIn: Boolean, position: Int) {
        shouldFadeIn = fadeIn
        //notifyItemChanged(position)
        if (slidesList.size > 1)
            notifyDataSetChanged()
    }

    fun updateList(itemList: List<Slide>) {
        slidesList = itemList
    }

}

