package com.breckworld.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.model.WalletModel
import com.breckworld.model.geostore.Card
import com.breckworld.model.geostore.Filter
import com.breckworld.model.geostore.Item
import com.breckworld.util.AppUtil
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import java.lang.Exception

class FilterIconAdapter(
    private var context: Context,
    private var itemList: List<Item>,
    private var selectedFilterIds: MutableList<Int>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter_icon, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)

        holderType.textViewTitle.text = itemList.get(position).title

        try {
            if (URLUtil.isValidUrl(itemList.get(position).icon)) {
                Glide.with(context).load(itemList.get(position).icon)
                    .into(holderType.imageViewIcon)
            } else {
                holderType.imageViewIcon.setImageDrawable(
                    AppUtil.findImageResource(
                        context,
                        itemList.get(position).icon
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (selectedFilterIds.contains(itemList.get(position).id)) {
            holderType.imageViewSelected.visibility = View.VISIBLE
        } else {
            holderType.imageViewSelected.visibility = View.GONE
        }

        holderType.layoutMain.setOnClickListener {
            if (itemList.get(position).isSelected) {
                itemList.get(position).isSelected = false
                onClickListener?.onFilterClickListener(position, false)
            } else {
                itemList.get(position).isSelected = true
                onClickListener?.onFilterClickListener(position, true)
            }
            notifyDataSetChanged()

        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var imageViewIcon: ImageView = itemView.findViewById(R.id.imageViewIcon)
        var imageViewSelected: ImageView = itemView.findViewById(R.id.imageViewSelected)
        var layoutMain: RelativeLayout = itemView.findViewById(R.id.layoutMain)
    }

    interface OnClickListener {
        fun onFilterClickListener(position: Int, needToAdd: Boolean)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}

