package com.breckworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.model.userprofile.Link

class NavigationAdapter(
    private var context: Context,
    private val linkItem: List<Link>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nav_menu, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)

        holderType.textViewTitle.text = linkItem.get(position).title
        holderType.textViewTitle.setOnClickListener { onClickListener?.onItemClickListener(position) }

        if (linkItem.get(position).shouldShowSpace && position != 0) {
            holderType.viewSpace.visibility = View.VISIBLE
        } else {
            holderType.viewSpace.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
       return linkItem.size
    }

    inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var viewSpace: View = itemView.findViewById(R.id.viewSpace)
    }

    interface OnClickListener {
        fun onItemClickListener(position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}

