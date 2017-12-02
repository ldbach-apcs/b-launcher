package vn.ldbach.blauncher.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import vn.ldbach.blauncher.Data.AppDetails
import vn.ldbach.blauncher.R
import vn.ldbach.blauncher.R.layout.list_item

/**
 * Created by ldbach on 12/2/17.
 */
class AppFragment : ViewFragment() {

    private lateinit var searchQuery: EditText
    private lateinit var listView: ListView
    private var listApps: List<AppDetails> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.viewfragment_app, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        searchQuery = view?.findViewById(R.id.search_query)!!
        listView = view.findViewById(R.id.lists_apps)!!

        listApps = retrieveApps()
        val adapter = MyArrayAdapter(this, this.context, list_item, listApps)
        listView.adapter = adapter

        addItemClickListener()

        searchQuery.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?)  {
                //if (s != null) {
                //    (s.length downTo 1)
                //            .filter { s.subSequence(it - 1, it).toString() == "\n" }
                //            .forEach { s.replace(it - 1, it, "") }
                //}
                //searchQuery.text = s
                filterApps(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun addItemClickListener() {
        listView.onItemClickListener = AdapterView.OnItemClickListener {
            _, _, position, _ -> run {
                val manager = this.context.packageManager
                val intent = manager.getLaunchIntentForPackage(listApps[position].name.toString())
                this.startActivity(intent)
            }
        }
    }

    private fun retrieveApps(): List<AppDetails> {
        val manager = this.context.packageManager
        val apps = ArrayList<AppDetails>()
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val availableActivities = manager.queryIntentActivities(intent, 0)
        availableActivities.mapTo(apps) {
            AppDetails(
                    label = it.loadLabel(manager),
                    name = it.activityInfo.packageName,
                    icon = it.activityInfo.loadIcon(manager)
            )
        }
        return apps
    }

    private fun filterApps(query: String) {
        val myAdapter = listView.adapter as MyArrayAdapter
        myAdapter.filter.filter(query)
    }

    class MyArrayAdapter (
            private var frag: android.support.v4.app.Fragment,
            mContext: Context,
            @LayoutRes layout:  Int,
            private var listApps: List<AppDetails>) :
            ArrayAdapter<AppDetails>(mContext, layout, listApps),
            Filterable {

        private val mFilter = AppFilter()
        var filteredAppList: List<AppDetails>

        init {
            filteredAppList = listApps
        }

        override fun getCount(): Int {
            return filteredAppList.size
        }

        override fun getItem(position: Int): AppDetails {
            return filteredAppList[position]
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var v = convertView
            if (v == null) {
                val layoutInflater = frag.layoutInflater
                v = layoutInflater.inflate(R.layout.list_item, null)
            }

            val appLabel: TextView = v!!.findViewById(R.id.label)
            appLabel.text = filteredAppList[position].label

            val appIcon: ImageView = v.findViewById(R.id.icon)
            appIcon.setImageDrawable(filteredAppList[position].icon)

            return v
        }

        override fun getFilter(): Filter {
            return mFilter
        }

        inner class AppFilter : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterString = constraint.toString()

                val nlist = ArrayList<AppDetails>()

                listApps.filter {
                    it.label.contains(filterString, true)
                }.forEach {
                    nlist.add(it)
                }

                val filterResult = FilterResults()
                filterResult.values = nlist
                filterResult.count = nlist.size

                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredAppList = results?.values as List<AppDetails>
                notifyDataSetChanged()
            }

        }
    }
}
