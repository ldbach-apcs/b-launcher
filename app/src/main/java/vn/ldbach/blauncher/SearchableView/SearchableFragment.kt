package vn.ldbach.blauncher.SearchableView

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import vn.ldbach.blauncher.ViewFragment
import vn.ldbach.blauncher.R
import vn.ldbach.blauncher.Utils.PermissionManager

/**
 * Searchable Fragment is responsible for displaying list of application and
 * provide interface for searching apps / contacts
 */
class SearchableFragment : ViewFragment() {

    // private lateinit var focusKeyboard: FloatingActionButton
    private lateinit var searchQuery: EditText
    private lateinit var listView: ListView
    private var adapter: SearchableArrayAdapter? = null
    private lateinit var searchableLists : ArrayList<List<Searchable>>
    private lateinit var dataManager: DataManager
    private lateinit var clearText: ImageButton

   // private var keyboardIsShowing = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.viewfragment_searchable, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        searchQuery = view?.findViewById(R.id.search_query)!!
        listView = view.findViewById(R.id.lists_apps)!!
        clearText = view.findViewById(R.id.clear_text)!!
        // focusKeyboard = view.findViewById(R.id.focus_keyboard)!!

        dataManager = DataManager(this.context)

        // initData()
        initInteraction()

        // focusKeyboard.requestFocus()
        val permManager = PermissionManager(context, this)

        // If shared preference is empty, request permision
        val pref = activity.application.getSharedPreferences(PermissionManager.PERM_FILE, Context
                .MODE_PRIVATE)
        if (!pref.contains(PermissionManager.CALL_PERM.toString()))
            permManager.requestCallPermission()
    }

    private fun initInteraction() {
        // addItemClickListener()

        listView.setOnScrollListener(object : AbsListView.OnScrollListener{
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                if (scrollState == SCROLL_STATE_IDLE) return
                hideKeyboard()
            }

            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

            }

        })


        searchQuery.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //if (s != null) {
                //    (s.length downTo 1)
                //            .filter { s.subSequence(it - 1, it).toString() == "\n" }
                //            .forEach { s.replace(it - 1, it, "") }
                //}
                //searchQuery.text = s
                resetListPosition()
                filterApps(s.toString())
                clearText.visibility = if (s.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        searchQuery.setOnClickListener {
            _ -> resetListPosition()
        }


        clearText.setOnClickListener { _-> searchQuery.text.clear() }
        // focusKeyboard.setOnClickListener({_ -> performFabAction()})
    }

    fun hideKeyboard() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        imm.hideSoftInputFromWindow(searchQuery.windowToken, 0)
    }

    private fun resetListPosition() {
        listView.apply {
            smoothScrollBy(0, 0)
            if (adapter != null)
                setSelection(adapter.count - 1)
        }
    }

    private fun initData() {
        searchableLists = ArrayList()

        // searchableLists.add(retrieveApps()) // (retrieveApps())
        searchableLists.add(dataManager.retrieveApps())

        // request list of contact
        val permManager = PermissionManager(this.context, this)
        if (permManager.hasContactPermission()) {
            searchableLists.add(dataManager.retrieveContacts())
        } else {
            permManager.requestContactPermission()
        }

        when (adapter) {
            null -> {
                adapter = SearchableArrayAdapter(this, searchableLists)
                listView.adapter = adapter
            }
            else -> {
                adapter!!.replaceList(searchableLists)
            }
        }
    }

    private fun filterApps(query: String) {
        // val myAdapter = listView.adapter as SearchableArrayAdapter
        adapter?.filter?.filter(query)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PermissionManager.CONTACT_PERM) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                searchableLists.add(dataManager.retrieveContacts())
            } else {
                // Notify the needs of contact permission.
            }
        }

        if (requestCode == PermissionManager.CALL_PERM) {
            // Put SharedPreference
            val settings = activity.applicationContext.getSharedPreferences(PermissionManager
                    .PERM_FILE, Context
                    .MODE_PRIVATE)
            val editor = settings.edit()
            editor.putBoolean(PermissionManager.CALL_PERM.toString(),
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED))
            editor.commit()
        }
    }

    private fun showKeyboard() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        imm.showSoftInput(searchQuery, InputMethodManager.SHOW_IMPLICIT)
        resetListPosition()
    }

    override fun onBackPressed() {
        hideKeyboard()
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }


    override fun onResume() {
        super.onResume()
        reload()
    }

    override fun onStart() {
        super.onStart()
        dataManager.registerReceiver()
    }

    override fun onStop() {
        dataManager.unregisterReceiver()
        super.onStop()
    }

    fun reload() {
        resetListPosition()
        searchQuery.requestFocus()
        searchQuery.text.clear()
        initData()
    }

    override fun onSelected() {
        showKeyboard()
    }

    override fun onDeselected() {
        hideKeyboard()
    }
}
