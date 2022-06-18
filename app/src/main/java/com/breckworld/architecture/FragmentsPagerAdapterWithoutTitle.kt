package com.breckworld.architecture

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * @author Dmytro Bondarenko
 * Date: 17.10.2018
 * Time: 14:07
 * E-mail: bondes87@gmail.com
 */
class FragmentsPagerAdapterWithoutTitle(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    private val mFragmentList = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
        notifyDataSetChanged()
    }
}