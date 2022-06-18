package com.breckworld.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.model.WalletCategoryModel
import com.breckworld.model.geostore.ColorsX
import com.breckworld.model.geostore.Tab

class WalletCategoryAdapter(
    private var context: Context,
    private var categoryList: List<Tab>,
    private var colors: ColorsX?,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wallet_category, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)

        holderType.textViewTitle.text = categoryList.get(position).title
        holderType.layoutBorder.setBackgroundTintList(ColorStateList.valueOf(
            Color.parseColor(
                colors?.tab_border
            )
        ))

        if (categoryList.get(position).isSelected) {
            holderType.textViewTitle.setBackgroundTintList(
                ColorStateList.valueOf(
                Color.parseColor(
                    colors?.tab_bg_active
                ))
            )
            holderType.textViewTitle.setTextColor(
                Color.parseColor(
                    colors?.tab_text_active
                )
            )
            holderType.textViewTitle.setTypeface(null, Typeface.BOLD)
            //holderType.textViewTitle.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_color)))

        } else {
            holderType.textViewTitle.setBackgroundTintList(
                ColorStateList.valueOf(
                Color.parseColor(
                    colors?.tab_bg
                ))
            )
            holderType.textViewTitle.setTextColor(
                Color.parseColor(
                    colors?.tab_text
                )
            )
            holderType.textViewTitle.setTypeface(null, Typeface.NORMAL)
            //holderType.textViewTitle.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.light_color)))
        }

        holderType.textViewTitle.setOnClickListener {

            for (i in categoryList.indices) {
                categoryList[i].isSelected = false;
            }
            categoryList.get(position).isSelected = true

            notifyDataSetChanged()

            onClickListener?.onCategoryClickListener(position)
        }

        if (categoryList.get(position).tab_count != 0 ) {
            holderType.textViewCounter.text = categoryList.get(position).tab_count.toString()
            holderType.textViewCounter.visibility = View.VISIBLE
        } else {
            holderType.textViewCounter.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount(): Int {
       return categoryList.size
    }

    inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var textViewCounter: TextView = itemView.findViewById(R.id.textViewCounter)
        var layoutBorder: LinearLayout = itemView.findViewById(R.id.layoutBorder)
    }

    interface OnClickListener {
        fun onCategoryClickListener(position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}

