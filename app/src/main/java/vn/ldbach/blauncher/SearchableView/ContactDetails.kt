package vn.ldbach.blauncher.SearchableView

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import vn.ldbach.blauncher.R

/**
 * Created by ldbach on 12/7/17.
 */
class ContactDetails(displayName: String, private val phoneNumber: String, thumbnail: Drawable) :
        Searchable(phoneNumber, displayName, thumbnail) {

    private var layoutView: View? = null

    override fun getIntent(): Intent {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel: $phoneNumber")
        return intent
    }

    override fun getView(convertView: View?, frag: Fragment): View? {
        if (layoutView == null) {
            val layoutInflater = frag.layoutInflater
            layoutView = layoutInflater.inflate(R.layout.list_contacts, null)

            val contactName: TextView = layoutView!!.findViewById(R.id.contact_name)
            contactName.text = searchString
            val contactIcon: ImageView = layoutView!!.findViewById(R.id.contact_icon)
            contactIcon.setImageDrawable(representIcon)
            val contactNumber: TextView = layoutView!!.findViewById(R.id.contact_number)
            contactNumber.text = phoneNumber
        }

        return layoutView
    }
}