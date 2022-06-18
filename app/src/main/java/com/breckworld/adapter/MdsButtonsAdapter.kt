package com.breckworld.adapter

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.model.mds.Button
import com.breckworld.util.AppUtil


class MdsButtonsAdapter(
    private val context: Context,
    private val buttonList: List<Button>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private lateinit var rocketAnimation: AnimationDrawable
    private var imageViewButtons: ImageView? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mds_buttons, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)

        imageViewButtons = holderType.imageViewButtons
        if (buttonList.get(position).labels != null) {
            holderType.textViewButtonTitle.visibility = View.VISIBLE
            holderType.textViewButtonTitle.text = buttonList.get(position).labels.title
        } else {
            holderType.textViewButtonTitle.visibility = View.GONE
        }

        if(buttonList.get(position).labels.equals("FAVORITE")) {
            val animation1: Animation = AnimationUtils.loadAnimation(context, R.anim.blink_animation)
            holderType.imageViewButtons.setBackgroundResource(R.drawable.fav_anim)
            holder.imageViewButtons.startAnimation(animation1)

           /* rocketAnimation = holderType.imageViewButtons.getBackground() as AnimationDrawable
            rocketAnimation.start()*/
        } else {
            holderType.imageViewButtons.background = null
        }

        try {
            if (buttonList.get(position).active) {
                holderType.imageViewButtons.setImageDrawable(
                    AppUtil.findImageResource(
                        context,
                        buttonList.get(position).images.icon_active
                    )
                )
                //holderType.imageViewButtons.background = ContextCompat.getDrawable(context, R.drawable.selected_button)
            } else {
                holderType.imageViewButtons.setImageDrawable(
                    AppUtil.findImageResource(
                        context,
                        buttonList.get(position).images.icon
                    )
                )
                //holderType.imageViewButtons.background = ContextCompat.getDrawable(context, R.drawable.bg_circle_black)
            }
        } catch (e: Exception) { e.printStackTrace() }

        holderType.layoutButton.setOnClickListener {

            /*for (i in buttonList.indices) {
                if (position == i) {
                    if (!buttonList[position].active)
                        buttonList[position].active = true
                } else buttonList[i].active = false
            }*/

            onClickListener?.onButtonClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return buttonList.size
    }

    inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
        var layoutButton: RelativeLayout = itemView.findViewById(R.id.layoutButton)
        var imageViewButtons: ImageView = itemView.findViewById(R.id.imageViewButtons)
        var textViewButtonTitle: TextView = itemView.findViewById(R.id.textViewButtonTitle)
    }

    interface OnClickListener {
        fun onButtonClickListener(position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun updateFavIcon(b: Boolean, position: Int) {
        if (b) {
            buttonList[position].active = true
        } else {
            buttonList[position].active = false
            //imageViewButtons?.background = null
            //rocketAnimation.stop()
        }
        notifyItemChanged(position)
    }

}

