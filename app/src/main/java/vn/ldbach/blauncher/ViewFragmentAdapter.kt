package vn.ldbach.blauncher

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import vn.ldbach.blauncher.Fragments.*
import vn.ldbach.blauncher.NoteView.NoteFragment
import vn.ldbach.blauncher.SearchableView.SearchableFragment

/**
 * Adapter for ViewFragmentPager
 */
class ViewFragmentAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private val numPage = 3
    private val fragments = ArrayList<ViewFragment>()

    init {
        fragments.add(NoteFragment())
        fragments.add(SearchableFragment())
        fragments.add(PhotoFragment())
    }

    override fun getItem(position: Int): Fragment = when (position) {
            in 0..(numPage - 1) -> fragments[position]
            else -> throw NoSuchElementException()
        }

    override fun getCount(): Int {
        return numPage
    }
}