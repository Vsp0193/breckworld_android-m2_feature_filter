package com.breckworld.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.breckworld.R
import com.breckworld.model.IntroModel


class IntroAdapter(private var context: Context?, private var itemList: ArrayList<IntroModel>?) :
    PagerAdapter() {

    override fun getCount(): Int {
        return itemList!!.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val myLayout: View = LayoutInflater.from(context).inflate(R.layout.item_intro, view, false)
        val textViewTitle: TextView = myLayout.findViewById(R.id.textViewTitle) as TextView
        val textViewDes: TextView = myLayout.findViewById(R.id.textViewDes) as TextView

        textViewTitle.text = itemList?.get(position)?.textTitle
        textViewDes.text = itemList?.get(position)?.textDes
        view.addView(myLayout, 0);
        return myLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //super.destroyItem(container, position, `object`)
        container.removeView(`object` as View?)
    }

}