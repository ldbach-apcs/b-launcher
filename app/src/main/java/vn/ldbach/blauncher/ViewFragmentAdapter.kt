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

    private var numPage = 3
    private val fragments = ArrayList<ViewFragment>()

    init {
        fragments.add(NoteFragment())
        fragments.add(SearchableFragment())
        fragments.add(PhotoFragment())
        numPage = fragments.size
    }

    override fun getItem(position: Int): Fragment = when (position) {
            in 0..(numPage - 1) -> fragments[position]
            else -> throw NoSuchElementException()
        }

    override fun getCount(): Int {
        return numPage
    }

    fun selectPageAtPosition(position: Int) {
        for (i in 0 until numPage) {
            if (i == position) fragments[i].onSelected()
            else fragments[i].onDeselected()
        }
    }
}