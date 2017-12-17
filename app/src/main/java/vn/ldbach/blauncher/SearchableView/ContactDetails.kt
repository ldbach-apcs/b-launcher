package vn.ldbach.blauncher.SearchableView

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri

/**
 * Created by ldbach on 12/7/17.
 */
class ContactDetails(displayName: String, private val phoneNumber: String, thumbnail: Drawable) :
        Searchable(phoneNumber, displayName, thumbnail) {

    override fun getIntent(): Intent {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel: $phoneNumber")
        return intent
    }
}