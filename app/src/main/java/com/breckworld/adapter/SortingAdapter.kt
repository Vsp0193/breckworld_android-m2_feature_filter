package com.breckworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.model.geostore.Sort

class SortingAdapter(
    private var context: Context,
    private var itemList: List<Sort>,
    private var selectedSorting: String?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_sorting, parent, false)
        return ViewHolderForSorting(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForSorting)

        holderType.textViewTitle.text = itemList.get(position).name

        if (itemList.get(position).id.equals(selectedSorting)) {
            holderType.layoutRootTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.header_color))
            holderType.textViewTitle.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        } else {
            holderType.layoutRootTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTransparent))
            holderType.textViewTitle.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
        }

        if(position == itemList.size -1) {
            holderType.view.visibility = View.GONE
        } else {
            holderType.view.visibility = View.VISIBLE
        }

        holderType.layoutRootTitle.setOnClickListener {
            for (i in itemList.indices) {
                itemList.get(i).active = false
            }
            itemList.get(position).active = true
            onClickListener?.onSortingClickListener(position)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolderForSorting(view: View) : RecyclerView.ViewHolder(view) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var layoutRootTitle: RelativeLayout = itemView.findViewById(R.id.layoutRootTitle)
        var view: View = itemView.findViewById(R.id.view)
    }

    interface OnClickListener {
        fun onSortingClickListener(position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}

