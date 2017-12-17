package vn.ldbach.blauncher.SearchableView

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import vn.ldbach.blauncher.R

/**
 * help maintain List of Apps
 */
data class AppDetails(
        val context: Context, val label: CharSequence, val name: CharSequence, val icon: Drawable)
    : Searchable(name, label, icon) {
    override fun getIntent(): Intent {
        val manager = this.context.packageManager
        return manager.getLaunchIntentForPackage(name.toString())
    }

    override fun getView(convertView: View?, frag: Fragment) : View? {
        //var v = convertView
        //if (v == null) {
        val layoutInflater = frag.layoutInflater
        val v = layoutInflater.inflate(R.layout.list_apps, null)
        //}

        val appLabel: TextView = v!!.findViewById(R.id.app_name)
        appLabel.text = searchString
        val appIcon: ImageView = v.findViewById(R.id.app_icon)
        appIcon.setImageDrawable(representIcon)

        return v
    }
}