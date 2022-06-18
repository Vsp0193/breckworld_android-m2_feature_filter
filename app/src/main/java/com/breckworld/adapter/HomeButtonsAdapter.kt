package com.breckworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.model.home.Button
import com.breckworld.util.AppUtil

class HomeButtonsAdapter(
    private val context: Context,
    private val buttonList: List<Button>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mds_buttons, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)

        holderType.textViewButtonTitle.text = buttonList.get(position).labels.title
        if (buttonList.get(position).active) {
            holderType.imageViewButtons.setImageDrawable(AppUtil.findImageResource(context, buttonList.get(position).images.icon_active))
            //holderType.imageViewButtons.background = ContextCompat.getDrawable(context, R.drawable.selected_button)
        } else {
            holderType.imageViewButtons.setImageDrawable(AppUtil.findImageResource(context, buttonList.get(position).images.icon))
            //holderType.imageViewButtons.background = ContextCompat.getDrawable(context, R.drawable.bg_circle_black)
        }

        holderType.layoutButton.setOnClickListener {

            var prevSelectedPosition = 0

            for (i in buttonList.indices) {
                if (buttonList[i].active)
                    prevSelectedPosition = i
            }

            for (i in buttonList.indices) {
                if (position == i) {
                    if (!buttonList[position].active)
                        buttonList[position].active = true
                }
                else buttonList[i].active = false
            }
            notifyDataSetChanged()
            onClickListener?.onButtonClickListener(position, prevSelectedPosition)
        }
    }

    override fun getItemCount(): Int {
       return buttonList.size
    }

    inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
        var layoutButton : RelativeLayout = itemView.findViewById(R.id.layoutButton)
        var imageViewButtons: ImageView = itemView.findViewById(R.id.imageViewButtons)
        var textViewButtonTitle: TextView = itemView.findViewById(R.id.textViewButtonTitle)
    }

    interface OnClickListener {
        fun onButtonClickListener(position: Int, prevSelectedPosition: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}

