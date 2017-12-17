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
        val CONTACT_PERM: Int = 123
    }

    fun hasContactPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    }

    fun requestContactPermission() {
        ActivityCompat.requestPermissions(frag.activity,
                arrayOf( Manifest.permission.READ_CONTACTS),
                CONTACT_PERM)
    }
}