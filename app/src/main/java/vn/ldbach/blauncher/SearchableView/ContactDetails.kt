package vn.ldbach.blauncher.SearchableView

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.app.Fragment
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
class ContactDetails(private val displayName: String, private val phoneNumber: String, thumbnail:
Drawable) :
        Searchable(displayName, thumbnail) {

    companion object {
        @JvmStatic
        var canCall = false
    }

    private lateinit var dialog: AlertDialog

    inner class DialogClickListener(private val context: Context)
                                            : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.contact_dial -> run {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:$phoneNumber")
                    context.startActivity(intent)
                }
                R.id.contact_msg -> run {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("sms:$phoneNumber")
                    context.startActivity(intent)
                }
                R.id.contact_info -> throw NotImplementedError()
                else -> throw UnsupportedOperationException()
            }

            dialog.dismiss()
        }
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

    @SuppressLint("InflateParams")
    override fun setOnLongClick(frag: Fragment) {

        val context = frag.context
        // val permManager = PermissionManager(frag.context, frag)

       // if (!permManager.hasCallPermission())
       //     permManager.requestCallPermission()

        val prefContext = frag.activity.applicationContext
        val settings = prefContext.getSharedPreferences(PermissionManager.PERM_FILE, Context.MODE_PRIVATE)
        canCall = settings.getBoolean(PermissionManager.CALL_PERM.toString(), false)


        layoutView?.setOnLongClickListener {
            _ -> run {
            // Create Dialog

            val dialogBuilder = AlertDialog.Builder(context)
            val view = frag.layoutInflater.inflate(R.layout.dialog_searchable_contact, null)

            // Create onClick of dialog Item
            val dialogClickListener = DialogClickListener(context)
            view.findViewById<TextView>(R.id.contact_dial).setOnClickListener(dialogClickListener)
            view.findViewById<TextView>(R.id.contact_msg).setOnClickListener(dialogClickListener)
            view.findViewById<TextView>(R.id.contact_info).setOnClickListener(dialogClickListener)

            view.findViewById<TextView>(R.id.contact_title).text = displayName

            dialogBuilder.setView(view)
            dialogBuilder.setCancelable(true)
            dialog = dialogBuilder.create()
            dialog.show()
            return@setOnLongClickListener true
        }}
    }
}