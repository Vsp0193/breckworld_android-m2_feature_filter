package com.breckworld.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.model.WalletModel
import com.breckworld.model.geostore.Card
import com.breckworld.util.AppUtil
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class GeoStoreAdapter(
    private var context: Context,
    private var itemList: List<Card>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wallet2, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)
        val item: Card = itemList.get(position)

        Picasso.get().load(item.images.logo).into(holderType.imageViewLogo)
        Glide.with(context).load(item.images.slide_card).into(holderType.imageViewMain)
        holderType.imageViewDistance.setImageDrawable(AppUtil.findImageResource(context, item.images.footer))
        holderType.imageViewIcon.setImageDrawable(AppUtil.findImageResource(context, item.images.icon))

        holderType.textViewCompanyName.text = item.labels.header
        holderType.textViewTitle.text = item.labels.title
        holderType.textViewDes.text = item.descriptions.long
        holderType.textViewTown.text = item.labels.town

        if (item.labels.distance.equals("N/A")) {
            holderType.imageViewLocation.visibility = View.VISIBLE
            holderType.textViewDistance.visibility = View.GONE
        } else {
            holderType.imageViewLocation.visibility = View.GONE
            holderType.textViewDistance.visibility = View.VISIBLE
            holderType.textViewDistance.text = item.labels.distance
        }

        holderType.textViewTitle.setTextColor(Color.parseColor(
            item.colors.header_text
        ))

        holderType.textViewTown.setTextColor(Color.parseColor(
            item.colors.footer_text
        ))

        holderType.textViewDistance.setTextColor(Color.parseColor(
            item.colors.footer_text
        ))

        holderType.imageViewForHeaderAlpha.setBackgroundColor(
            Color.parseColor(
                item.colors.header_bg
            )
        )


        holderType.imageViewForHeaderAlpha.alpha =
            item.colors.header_opacity.toFloat()

        holderType.imageViewForFooterAlpha.setBackgroundColor(
            Color.parseColor(
                item.colors.footer_bg
            )
        )

        holderType.imageViewForFooterAlpha.alpha =
            item.colors.footer_opacity.toFloat()

        holderType.layoutMain.setOnClickListener {
            onClickListener?.onCategoryClickListener(position)
        }

        /*holderType.textViewDistance.setOnClickListener {
            onClickListener?.ontextViewDistanceClickListener(position)
        }*/

        holderType.imageViewLocation.setOnClickListener {
            onClickListener?.ontextViewDistanceClickListener(position)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
        var imageViewMain: ImageView = itemView.findViewById(R.id.imageViewMain)
        var imageViewLogo: ImageView = itemView.findViewById(R.id.imageViewLogo)
        var imageViewRewards: ImageView = itemView.findViewById(R.id.imageViewRewards)
        var imageViewDistance: ImageView = itemView.findViewById(R.id.imageViewDistance)
        var imageViewLocation: ImageView = itemView.findViewById(R.id.imageViewLocation)
        var imageViewIcon: ImageView = itemView.findViewById(R.id.imageViewIcon)
        var imageViewForHeaderAlpha: ImageView = itemView.findViewById(R.id.imageViewForHeaderAlpha)
        var imageViewForFooterAlpha: ImageView = itemView.findViewById(R.id.imageViewForFooterAlpha)
        var textViewCompanyName: TextView = itemView.findViewById(R.id.textViewCompanyName)
        var textViewCompanyAddress: TextView = itemView.findViewById(R.id.textViewCompanyAddress)
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var textViewDes: TextView = itemView.findViewById(R.id.textViewDes)
        var textViewDistance: TextView = itemView.findViewById(R.id.textViewDistance)
        var textViewRedeemed: TextView = itemView.findViewById(R.id.textViewRedeemed)
        var textViewTown: TextView = itemView.findViewById(R.id.textViewTown)
        var layoutHeader: RelativeLayout = itemView.findViewById(R.id.layoutHeader)
        var layoutMain: RelativeLayout = itemView.findViewById(R.id.layoutMain)
    }

    interface OnClickListener {
        fun onCategoryClickListener(position: Int)
        fun ontextViewDistanceClickListener(position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}

