package vn.ldbach.blauncher

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.view.ViewPager
import android.view.View

class MainActivity : AppCompatActivity() {

    private val pager by bind<ViewPager>(R.id.main_pager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPager()
    }

    fun initPager() {
        val pagerAdapter = ViewFragmentAdapter(supportFragmentManager)
        pager.adapter = pagerAdapter
        pager.currentItem = 1
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
