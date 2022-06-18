package com.breckworld.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.breckworld.model.mds.All
import com.breckworld.model.mds.Me
import com.breckworld.util.AppUtil
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import java.lang.Exception

class LeaderboardAdapter(
    private var context: Context,
    private var itemList: List<All>,
    private var me: Me?,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)

        if (me?.position!! > 1000 && (position == itemList.size - 1)) {
            holderType.layoutName.visibility = View.VISIBLE
            holderType.textViewMyName.text = "${me?.position}.  ${me?.name}"
        } else {
            holderType.layoutName.visibility = View.GONE
        }

        if (me?.position!! <= 1000 && (position + 1 == me?.position!!)) {
            holderType.textViewName.setTypeface(null, Typeface.BOLD)
            holderType.textViewName.setTextSize(18f)
        } else {
            holderType.textViewName.setTypeface(null, Typeface.NORMAL)
            holderType.textViewName.setTextSize(15f)
        }

        holderType.textViewName.text = "${position + 1}.  " + itemList.get(position).name
    }

    override fun getItemCount(): Int {
        if (itemList.size > 1000)
            return 1000
        else
            return itemList.size
    }

    inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView = itemView.findViewById(R.id.textViewName)
        var textViewMyName: TextView = itemView.findViewById(R.id.textViewMyName)
        var layoutName: LinearLayout = itemView.findViewById(R.id.layoutName)
    }

    interface OnClickListener {
        fun onNameClickListener(position: Int, needToAdd: Boolean)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}

