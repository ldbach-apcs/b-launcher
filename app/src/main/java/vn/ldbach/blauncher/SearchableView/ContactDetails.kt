package vn.ldbach.blauncher.SearchableView

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.widget.PopupMenu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import vn.ldbach.blauncher.R
import vn.ldbach.blauncher.Utils.PermissionManager
import vn.ldbach.blauncher.t9Contains

/**
 * This class is for helping to maintain Contact Detail
 */
class ContactDetails(
        private val id: Long, private val displayName: String, private val phoneNumber: String,
        thumbnail:
Drawable) :
        Searchable(displayName, thumbnail) {

    companion object {
        @JvmStatic
        var canCall = false
    }

    override fun isSearchableBy(query : String): Boolean {
        return displayName.t9Contains(query) or phoneNumber.t9Contains(query)
    }

    override fun getIntent(): Intent {

        // if user didn't allow call, send message instead
        val intent =
                if (canCall) Intent(Intent.ACTION_CALL)
                else Intent(Intent.ACTION_DIAL)

        //val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        return intent
    }

    override fun getView(convertView: View?, frag: Fragment, parent: ViewGroup?): View? {
        if (layoutView == null) {
            val layoutInflater = frag.layoutInflater
            layoutView = layoutInflater.inflate(R.layout.list_contacts, parent, false)

            val contactName: TextView = layoutView!!.findViewById(R.id.contact_name)
            contactName.text = searchString
            val contactIcon: ImageView = layoutView!!.findViewById(R.id.contact_icon)
            contactIcon.setImageDrawable(representIcon)
            val contactNumber: TextView = layoutView!!.findViewById(R.id.contact_number)
            contactNumber.text = phoneNumber
        }

        return layoutView
    }


    override fun setOnLongClick(frag: Fragment) {
        super.setOnLongClick(frag, R.menu.menu_contact_long_click)
    }

    override fun initMenuClick(frag: Fragment) {
        menuClick = PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.contact_dial -> run {
                    val dialIntent = Intent(Intent.ACTION_DIAL)
                    dialIntent.data = Uri.parse("tel:$phoneNumber")
                    frag.startActivity(dialIntent)
                    return@OnMenuItemClickListener true
                }
                R.id.contact_msg -> run {
                    val msgIntent = Intent(Intent.ACTION_VIEW)
                    msgIntent.data = Uri.parse("sms:$phoneNumber")
                    frag.startActivity(msgIntent)
                    return@OnMenuItemClickListener true
                }
                R.id.contact_info -> run {

                    return@OnMenuItemClickListener true
                }
                else -> return@OnMenuItemClickListener false
            }
        }
        isMenuClickDefaultBehavior = false
    }
}