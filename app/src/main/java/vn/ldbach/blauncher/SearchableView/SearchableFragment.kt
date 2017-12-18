package vn.ldbach.blauncher.SearchableView

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import vn.ldbach.blauncher.ViewFragment
import vn.ldbach.blauncher.R
import vn.ldbach.blauncher.Utils.PermissionManager

/**
 * Searchable Fragment is responsible for displaying list of application and
 * provide interface for searching apps / contacts
 */
class SearchableFragment : ViewFragment() {

    private lateinit var focusKeyboard: FloatingActionButton
    private lateinit var searchQuery: EditText
    private lateinit var listView: ListView
    private var adapter: SearchableArrayAdapter? = null
    private lateinit var searchableLists : ArrayList<List<Searchable>>
    private lateinit var dataManager: DataManager

   // private var keyboardIsShowing = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.viewfragment_searchable, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        searchQuery = view?.findViewById(R.id.search_query)!!
        listView = view.findViewById(R.id.lists_apps)!!
        focusKeyboard = view.findViewById(R.id.focus_keyboard)!!

        dataManager = DataManager(this.context)

        // initData()
        initInteraction()

        focusKeyboard.requestFocus()
    }

    private fun initInteraction() {
        // addItemClickListener()

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
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        searchQuery.setOnClickListener {
            _ -> resetListPosition()
        }


        // focusKeyboard.setOnClickListener({_ -> performFabAction()})
    }

    private fun resetListPosition() {
        listView.apply {
            smoothScrollBy(0, 0)
            setSelection(0)
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
                adapter!!.notifyDataSetChanged()}
        }
    }

    private fun addItemClickListener() {
        listView.onItemClickListener = AdapterView.OnItemClickListener {
            _, _, position, _ -> run {

                // Clear the search box in addition to launching app
                // searchQuery.text.clear()

               // val manager = this.context.packageManager
               // val intent = manager.getLaunchIntentForPackage(
               //         adapter.getItem(position).intentString.toString())

                val intent = adapter?.getItem(position)?.getIntent()
                if (intent != null)
                    this.startActivity(intent)
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
    }

    override fun performFabAction() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager

        //if (searchQuery.hasFocus()) {
        //    imm.hideSoftInputFromWindow(activity.currentFocus.windowToken, 0)
        //    searchQuery.clearFocus()
        //    focusKeyboard.requestFocus()
        //    Toast.makeText(context, "Hide now!", Toast.LENGTH_SHORT).show()
        //  //  keyboardIsShowing = false
        //} else {
        imm.showSoftInput(searchQuery, InputMethodManager.SHOW_IMPLICIT)
        //   Toast.makeText(context, "Show now!", Toast.LENGTH_SHORT).show()
            // keyboardIsShowing = false
        //}

        //searchQuery.apply {
        //    performClick()
        //    // performFabAction()
        //    clearFocus()
        //   if (requestFocus()) {
        //        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        //    }
        //}
    }

    override fun onResume() {
        super.onResume()
        resetListPosition()
        searchQuery.text.clear()
        initData()
    }
}
