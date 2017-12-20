package vn.ldbach.blauncher.Utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import java.security.Permission

/**
 * Permission manager is responsible for requesting runtime permission
 */
class PermissionManager(private val context: Context, private val frag: Fragment) {

    companion object {
        @JvmStatic
        val CONTACT_PERM = 123
        val CALL_PERM = 2307
        val PERM_FILE = "permission_file"
    }

    fun hasContactPermission(): Boolean = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

    fun hasCallPermission(): Boolean = ContextCompat.checkSelfPermission(context,
            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

    fun requestCallPermission() {
        ActivityCompat.requestPermissions(frag.activity,
                arrayOf( Manifest.permission.CALL_PHONE),
                CONTACT_PERM)
    }

    fun requestContactPermission() {
        ActivityCompat.requestPermissions(frag.activity,
                arrayOf( Manifest.permission.READ_CONTACTS),
                CALL_PERM)
    }
}