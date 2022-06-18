package com.breckworld.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.*
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.model.home.Banner
import com.breckworld.util.AppUtil
import com.bumptech.glide.Glide
import java.lang.Exception

class HomeBannersAdapter(
    private val context: Context,
    private var bannerList: List<Banner>?,
    private var bannerList2: List<com.breckworld.model.geostore.Banner>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_banners, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)

        if (bannerList != null) {
            val banner = bannerList?.get(position)
            holderType.textViewBannerLabel.text = banner?.label

            try {
                if (URLUtil.isValidUrl(banner?.icon)) {
                    Glide.with(context).load(banner?.icon)
                        .into(holderType.imageViewBannerIcon)
                } else {
                    holderType.imageViewBannerIcon.setImageDrawable(
                        AppUtil.findImageResource(
                            context,
                            banner?.icon!!
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            holderType.layoutBanner.setBackgroundColor(Color.parseColor(banner?.colors?.banner_bg))
            holderType.textViewBannerLabel.setTextColor(Color.parseColor(banner?.colors?.banner_text))

            /*if (position == bannerList?.size!! - 1) {
                holderType.viewDivider.visibility = View.GONE
            } else {
                holderType.viewDivider.visibility = View.VISIBLE
            }*/
        }
        else if (bannerList2 != null) {
            val banner = bannerList2?.get(position)
            holderType.textViewBannerLabel.text = banner?.label

            try {
                if (URLUtil.isValidUrl(banner?.icon)) {
                    Glide.with(context).load(banner?.icon)
                        .into(holderType.imageViewBannerIcon)
                } else {
                    holderType.imageViewBannerIcon.setImageDrawable(
                        AppUtil.findImageResource(
                            context,
                            banner?.icon!!
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            holderType.layoutBanner.setBackgroundColor(Color.parseColor(banner?.colors?.banner_bg))
            holderType.textViewBannerLabel.setTextColor(Color.parseColor(banner?.colors?.banner_text))

            /*if (position == bannerList2?.size!! - 1) {
                holderType.viewDivider.visibility = View.GONE
            } else {
                holderType.viewDivider.visibility = View.VISIBLE
            }*/
        }

            holderType.layoutBanner.setOnClickListener {
                onClickListener?.onBannerClickListener(position)
            }
        }

        override fun getItemCount(): Int {
            if (bannerList != null) {
                return bannerList?.size!!
            } else {
                return bannerList2?.size!!
            }
        }

        inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
            var layoutBanner: LinearLayout = itemView.findViewById(R.id.layoutBanner)
            var imageViewBannerIcon: ImageView = itemView.findViewById(R.id.imageViewBannerIcon)
            var imageViewOpenLink: ImageView = itemView.findViewById(R.id.imageViewOpenLink)
            var textViewBannerLabel: TextView = itemView.findViewById(R.id.textViewBannerLabel)
            //var viewDivider: View = itemView.findViewById(R.id.viewDivider)
        }

        interface OnClickListener {
            fun onBannerClickListener(position: Int)
        }

        fun setOnClickListener(onClickListener: OnClickListener) {
            this.onClickListener = onClickListener
        }

    }

