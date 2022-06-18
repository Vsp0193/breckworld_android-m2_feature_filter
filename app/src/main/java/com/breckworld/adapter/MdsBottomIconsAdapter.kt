package com.breckworld.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.model.WalletModel
import com.breckworld.model.mds.Colors
import com.breckworld.model.mds.Footer
import com.breckworld.util.AppUtil
import com.bumptech.glide.Glide
import java.lang.Exception

class MdsBottomIconsAdapter(
    private var context: Context,
    private var itemList: List<Footer>,
    private var colors: Colors,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mds_bottom_button, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)
        val item: Footer = itemList.get(position)
        try {
            holderType.imageViewIcons.setImageDrawable(
                AppUtil.findImageResource(
                    context,
                    item.images.icon
                )
            )
        } catch (e: Exception) { e.printStackTrace() }
        holderType.textViewTitle.text = item.labels.title

        //holderType.textViewTitle.setTextColor(Color.parseColor(colors.footer_text))
        //holderType.imageViewIcons.setColorFilter(Color.parseColor(colors.footer_text), android.graphics.PorterDuff.Mode.MULTIPLY)

        holderType.layoutButton.setOnClickListener {
            onClickListener?.onIconClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
        var imageViewIcons: ImageView = itemView.findViewById(R.id.imageViewIcons)
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var layoutButton: LinearLayout = itemView.findViewById(R.id.layoutButton)
    }

    interface OnClickListener {
        fun onIconClickListener(position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}

