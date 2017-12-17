package vn.ldbach.blauncher

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager
import android.view.View
import vn.ldbach.blauncher.SearchableView.SearchableFragment

class MainActivity : AppCompatActivity() {

    private val pager by bind<ViewPager>(R.id.main_pager)
    private val fab by bind<FloatingActionButton>(R.id.fab_view_action)
    private lateinit var pagerAdapter: ViewFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPager()
        initButton()
    }

    private fun initButton() {
        fab.setOnClickListener({
            _ -> run {
                val curPos = pager.currentItem
            (pagerAdapter.getItem(curPos) as ViewFragment).performFabAction()
            }})
    }

    override fun onBackPressed() {
        // Wont do anything on back button clicked
    }

    private fun initPager() {
        pagerAdapter = ViewFragmentAdapter(supportFragmentManager)
        pager.adapter = pagerAdapter
        pager.currentItem = 1

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) = when (pagerAdapter.getItem(position)) {
                is SearchableFragment -> fab.show()
                    else -> fab.hide()
                }
            })
    }


    private fun <T: View> Activity.bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE) {
            findViewById<T>(res)
        }
    }

    fun <T: View> View.bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE) {
            findViewById<T>(res)
        }
    }
}
