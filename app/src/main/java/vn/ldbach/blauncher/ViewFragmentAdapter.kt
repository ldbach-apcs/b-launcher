package vn.ldbach.blauncher

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import vn.ldbach.blauncher.Fragments.*

/**
 * Created by ldbach on 12/2/17.
 */
class ViewFragmentAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    val numPage = 3

    override fun getItem(position: Int): Fragment = when (position) {
            0 -> PedoFragment()
            1 -> AppFragment()
            2 -> PhotoFragment()
            else -> throw NoSuchElementException()
        }


    override fun getCount(): Int {
        return numPage
    }
}