package vn.ldbach.blauncher.SearchableView

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import vn.ldbach.blauncher.R

/**
 * help maintain List of Apps
 */
data class AppDetails(
        val context: Context, val label: CharSequence, val name: CharSequence, val icon: Drawable)
    : Searchable(label, icon) {

    override fun getIntent(): Intent {
        val manager = this.context.packageManager
        return manager.getLaunchIntentForPackage(name.toString())
    }

    override fun getView(convertView: View?, frag: Fragment, parent: ViewGroup?) : View? {
        if (layoutView == null) {
            val layoutInflater = frag.layoutInflater
            layoutView = layoutInflater.inflate(R.layout.list_apps, parent, false)
            val appLabel: TextView = layoutView!!.findViewById(R.id.app_name)
            appLabel.text = searchString
            val appIcon: ImageView = layoutView!!.findViewById(R.id.app_icon)
            appIcon.setImageDrawable(representIcon)
        }
        return layoutView
    }

    override fun setOnLongClick(frag: Fragment) {
        layoutView?.setOnLongClickListener({
            _ -> run {
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$name" )
            context.startActivity(intent)
            return@setOnLongClickListener true
        }})

    }
}