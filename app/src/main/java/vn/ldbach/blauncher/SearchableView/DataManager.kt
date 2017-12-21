package vn.ldbach.blauncher.SearchableView

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import vn.ldbach.blauncher.R


/**
 * DataManager class is responsible for retrieving list of application
 * list of contact, and list of photo.
 */
class DataManager(private val context: Context) {

    private val contactList = ArrayList<ContactDetails>()
    private val appList = ArrayList<AppDetails>()
    private val filter = IntentFilter()

    init {

        filter.addAction(Intent.ACTION_PACKAGE_ADDED)
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        filter.addDataScheme("package")
    }

    fun registerReceiver() {
        context.registerReceiver(receiver, filter)
    }

    fun unregisterReceiver() {
        context.unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null )
                return

            if (intent.action == Intent.ACTION_PACKAGE_ADDED)
                addApp(intent)

            if (intent.action == Intent.ACTION_PACKAGE_REMOVED)
                removeApp(intent)
        }
    }

    fun removeApp(intent: Intent) {
        val packageName = intent.data.schemeSpecificPart
        if (packageName.isEmpty()) return

        for (app in appList) {
            if (app.name == packageName) {
                appList.remove(app)
                break
            }
        }
    }

    fun addApp(intent: Intent) {
        val packageName = intent.data.schemeSpecificPart
        if (packageName.isEmpty()) return

        try {
            val packageManager = context.packageManager
            val app = packageManager.getApplicationInfo(packageName, 0)

            val icon = packageManager.getApplicationIcon(app)
            val label = packageManager.getApplicationLabel(app)

            appList.add(AppDetails(context = context, name = packageName, icon = icon, label = label))
        } catch (e : PackageManager.NameNotFoundException) {
            // Do nothing
        }
    }

    // These methods should only be called from SearchViewFragment only!
    fun retrieveApps() : List<AppDetails> {

        if (appList.isEmpty()) {

            val manager = context.packageManager
            // val appList = ArrayList<AppDetails>()
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)

            val availableActivities = manager.queryIntentActivities(intent, 0)

            availableActivities.mapTo(appList) {
                AppDetails(
                        context = this.context,
                        label = it.loadLabel(manager),
                        name = it.activityInfo.packageName,
                        icon = it.activityInfo.loadIcon(manager)
                )
            }
        }

        return appList.sortedWith(Comparator { app1, app2 ->
            app2.label.toString().compareTo(app1.label.toString(), ignoreCase = true)
        })
    }

    // RetrieveContacts should only be called after accepting contact permission.
    fun retrieveContacts(): List<ContactDetails> {

        if (contactList.isEmpty()) {
            var contactDrawable: Drawable
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
        }

        return contactList
    }


}