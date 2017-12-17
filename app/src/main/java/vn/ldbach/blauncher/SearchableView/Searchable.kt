package vn.ldbach.blauncher.SearchableView

import android.content.Intent
import android.graphics.drawable.Drawable

/**
 * Created by ldbach on 12/6/17.
 */
abstract class Searchable(val intentString: CharSequence, val searchString: CharSequence,
                     val representIcon: Drawable) {
    abstract fun getIntent() : Intent
}