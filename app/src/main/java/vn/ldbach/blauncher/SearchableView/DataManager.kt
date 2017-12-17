package vn.ldbach.blauncher.SearchableView

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.ContactsContract
import vn.ldbach.blauncher.R
import java.io.FileNotFoundException


/**
 * DataManager class is responsible for retrieving list of application
 * list of contact, and list of photo.
 */
class DataManager(private val context: Context) {

    // These methods should only be called from SearchViewFragment only!
    fun retrieveApps() : List<AppDetails> {
        val manager = context.packageManager
        val apps = ArrayList<AppDetails>()
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val availableActivities = manager.queryIntentActivities(intent, 0)

        availableActivities.mapTo(apps) {
            AppDetails(
                    context = this.context,
                    label = it.loadLabel(manager),
                    name = it.activityInfo.packageName,
                    icon = it.activityInfo.loadIcon(manager)
            )
        }

        return apps.sortedWith(Comparator { app1, app2 ->
            app1.label.toString().compareTo(app2.label.toString(), ignoreCase = true)
        })
    }

    // RetrieveContacts should only be called after accepting contact permission.
    fun retrieveContacts(): List<ContactDetails> {
        var contactDrawable: Drawable

        val contactList = ArrayList<ContactDetails>()
        val phones = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone
                .CONTENT_URI, null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC")

        while (phones.moveToNext()) {
            val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val imgUriString = phones.getString(
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

            var imgUri: Uri

            if (imgUriString != null) {
                imgUri = Uri.parse(imgUriString)
                contactDrawable = try {
                    val inputStream = context.contentResolver.openInputStream(imgUri)
                    Drawable.createFromStream(inputStream, imgUri.toString())
                } catch (e: Exception) {
                    context.getDrawable(R.drawable.ic_contact_default)
                }
            } else {
                contactDrawable = context.getDrawable(R.drawable.ic_contact_default)
            }

            contactList.add(ContactDetails(name, phoneNumber, contactDrawable))
        }
        phones.close()


        return contactList
    }


}