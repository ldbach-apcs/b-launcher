package vn.ldbach.blauncher.SearchableView

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.view.View

/**
 * Interface template for Searchable items
 */
abstract class Searchable(val searchString: CharSequence, val representIcon: Drawable) {

    protected var layoutView: View? = null
    abstract fun getIntent() : Intent
    abstract fun getView(convertView : View?, frag : Fragment) : View?
    abstract fun setOnLongClick(context: Context)

    fun setOnClick(context: Context) {
        layoutView?.setOnClickListener({
            _ -> context.startActivity(getIntent())
        })
    }
}