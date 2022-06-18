package com.breckworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.model.geostore.Filter

class FilterAdapter(
    private var context: Context,
    private var itemList: List<Filter>,
    private var isClearFilter: Boolean,
    private var selectedFilterIds: MutableList<Int>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter, parent, false)
        return ViewHolderForNewUser(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderType = (holder as ViewHolderForNewUser)

        holderType.textViewTitle.text = itemList.get(position).title

        if (!isClearFilter) {
            for (i in itemList.indices) {
                for (j in itemList.get(i).items.indices) {
                    if ((selectedFilterIds.contains(itemList.get(i).items.get(j).id))) {
                        itemList.get(i).items.get(j).isSelected = true
                        onClickListener?.onFilterClickListener(j, true, i)
                    } else {
                        itemList.get(i).items.get(j).isSelected = false
                    }
                }
            }
        } else {
            selectedFilterIds.clear()
            /*for (i in itemList.indices) {
                for (j in itemList.get(i).items.indices) {
                    itemList.get(i).items.get(j).isSelected = false
                }
            }*/
        }

        val filterIconAdapter = FilterIconAdapter(
            context,
            itemList.get(position).items,
            selectedFilterIds
        )
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 4)
        holderType.recyclerViewFilterIcons.setLayoutManager(mLayoutManager)
        holderType.recyclerViewFilterIcons.adapter = filterIconAdapter
        filterIconAdapter.setOnClickListener(object :
            FilterIconAdapter.OnClickListener {
            override fun onFilterClickListener(pos: Int, needToAdd: Boolean) {
                onClickListener?.onFilterClickListener(pos, needToAdd, position)
            }
        })

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolderForNewUser(view: View) : RecyclerView.ViewHolder(view) {
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var recyclerViewFilterIcons: RecyclerView = itemView.findViewById(R.id.recyclerViewFilterIcons)
    }

    interface OnClickListener {
        fun onFilterClickListener(position: Int, needToAdd: Boolean, outerPosition: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}

