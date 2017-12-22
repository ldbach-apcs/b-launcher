package vn.ldbach.blauncher.SearchableView

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v7.widget.PopupMenu
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
            it -> run {
            /*
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$name" )
            context.startActivity(intent)
            */

            val popup = PopupMenu(context, it)
            frag.activity.menuInflater.inflate(R.menu.menu_app_long_click, popup.menu)

            popup.setOnMenuItemClickListener {
                item  -> when(item.itemId) {
                    R.id.app_info -> run {
                        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:$name" )
                        context.startActivity(intent)
                        return@setOnMenuItemClickListener  true
                    }
                    R.id.app_delete -> run {
                        // Show confirm dialog
                        val uri = Uri.parse("package:$name")
                        val uninstall = Intent(Intent.ACTION_UNINSTALL_PACKAGE, uri)
                        context.startActivity(uninstall)
                        // dialog.dismiss()

                        //val dialog = dialogBuilder.create().show()
                        return@setOnMenuItemClickListener  true
                    }
                    else -> return@setOnMenuItemClickListener false
            }}

            popup.show()
            return@setOnLongClickListener true
        }})

    }
}