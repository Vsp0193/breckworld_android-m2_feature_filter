package com.breckworld.architecture

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.breckworld.BR

abstract class BaseRecyclerViewAdapter<out S : BaseItemState, in V : ViewDataBinding, I : Any>(
    protected var list: MutableList<I>?,
    open val listener: Listener<I>
) : androidx.recyclerview.widget.RecyclerView.Adapter<BaseRecyclerViewAdapter<S, V, I>.BaseViewHolder<V>>() {

    protected abstract fun itemState(item: I, listener: Listener<I>): S

    @LayoutRes
    protected abstract fun layoutResId(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<V> {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = DataBindingUtil.inflate<V>(inflater, layoutResId(), parent, false)
        return BaseViewHolder<V>(itemBinding)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<V>,
        position: Int
    ) {
        holder.bindItem(list!![position], position)
    }

    override fun getItemCount() = list?.size ?: 0

    interface Listener<I> {
        fun onItemClick(item: I)
    }

    inner class BaseViewHolder<out V : ViewDataBinding>(
        val binding: V
    ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: I, position: Int) {
            val viewModel = itemState(item, listener)
            binding.setVariable(BR.state, viewModel)
            binding.executePendingBindings()
        }
    }

    fun replace(newList: List<I>?) {
        setNewItems(newList)
        notifyDataSetChanged()
    }

    fun setNewItems(newList: List<I>?) {
        list = newList?.toMutableList() ?: ArrayList()
    }

    fun insertItem(item: I) {
        list?.add(item)
        notifyItemInserted((list?.size?.minus(1)) ?: 0)
    }

    fun getItem(position: Int): I {
        return list!![position]
    }

    fun addAll(items: List<I>?) {
        val size = list?.size ?: 0
        items?.let {
            list?.addAll(items)
            notifyItemRangeInserted(size, items.size)
        }
    }

    fun getItemPosition(item: I): Int {
        return list?.indexOf(item)!!
    }

    fun removeItem(position: Int) {
        list?.let {
            it.removeAt(position)
            notifyItemRemoved(position)
            val remain = it.size - position
            if (remain > 0) notifyItemRangeChanged(position, remain)
        }
    }

    fun removeAll() {
        list?.clear()
        notifyDataSetChanged()
    }

    fun getItems(): List<I> = list!!
}