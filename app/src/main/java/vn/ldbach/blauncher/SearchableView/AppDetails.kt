package vn.ldbach.blauncher.SearchableView

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable

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
}