package vn.ldbach.blauncher.SearchableView

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.annotation.MenuRes
import android.support.v4.app.Fragment
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.PopupMenu
import android.view.View
import android.view.ViewGroup
import vn.ldbach.blauncher.R
import vn.ldbach.blauncher.t9Contains

/**
 * Interface template for Searchable items
 */
abstract class Searchable(val searchString: CharSequence, val representIcon: Drawable) {

    protected var layoutView: View? = null

    // default: do nothing
    protected var menuClick = PopupMenu.OnMenuItemClickListener { _ ->
        return@OnMenuItemClickListener false
    }
    protected var isMenuClickDefaultBehavior = true

    abstract fun getIntent() : Intent
    abstract fun getView(convertView: View?, frag: Fragment, parent: ViewGroup?) : View?
    abstract fun setOnLongClick(frag: Fragment)
    abstract fun initMenuClick(frag: Fragment)

    open fun isSearchableBy(query : String): Boolean {
        return searchString.t9Contains(query)
    }

    fun setOnClick(context: Context) {
        layoutView?.setOnClickListener({
            _ -> context.startActivity(getIntent())
        })
    }

    @SuppressLint("RestrictedApi")
    protected fun setOnLongClick(frag: Fragment, @MenuRes menu: Int) {
        if (isMenuClickDefaultBehavior) initMenuClick(frag)
        layoutView?.setOnLongClickListener({
            it -> run {
            val wrapper = ContextThemeWrapper(frag.context, R.style.PopupTheme)
            val popup = PopupMenu(wrapper, it)
            frag.activity.menuInflater.inflate(menu, popup.menu)
            popup.setOnMenuItemClickListener(menuClick)

            popup.show()
            return@setOnLongClickListener true
        }})
    }
}