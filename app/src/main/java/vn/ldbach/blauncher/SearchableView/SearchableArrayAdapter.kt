package vn.ldbach.blauncher.SearchableView

import android.view.View
import android.view.ViewGroup
import android.widget.*

/**
 * Adapter for SearchView in the Launcher
 */
class SearchableArrayAdapter (
        private var frag: android.support.v4.app.Fragment,
        private var searchableLists: ArrayList<List<Searchable>>) :
        BaseAdapter(),
        //ArrayAdapter<List<Searchable>>(mContext, layout, searchableLists),
        Filterable {
    override fun getItemId(position: Int): Long {
        return 0
    }

    private val mFilter = AppFilter()
    var filteredAppList: List<Searchable>

    init {
        filteredAppList = searchableLists[0]
    }

    override fun getCount(): Int {
        return filteredAppList.size
    }

    override fun getItem(position: Int): Searchable {
        return filteredAppList[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        /*
        var v = convertView
        if (v == null) {
            val layoutInflater = frag.layoutInflater
            v = layoutInflater.inflate(R.layout.list_item, null)
        }

        val appLabel: TextView = v!!.findViewById(R.id.label)
        appLabel.text = filteredAppList[position].searchString

        val appIcon: ImageView = v.findViewById(R.id.icon)
        appIcon.setImageDrawable(filteredAppList[position].representIcon)

        return v
        */
        val v =  filteredAppList[position].getView(convertView, frag, parent)
        filteredAppList[position].apply {
            setOnClick(frag.context)
            setOnLongClick(frag.context)
        }
        return v
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    inner class AppFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterString = constraint.toString()

            val nlist = ArrayList<Searchable>()

            // searchableLists.filter {
            //   //it.label.t9Contains(filterString)
            //    it.label.contains(filterString, true)
            //}.forEach {
            //    nlist.add(it)
            //}

            if (!filterString.isEmpty()) {
                for (searchable in searchableLists) {
                    searchable.filter {
                        it.isSearchableBy(filterString)
                    }.forEach {
                        nlist.add(it)
                    }
                }
            } else {
                searchableLists[0].forEach {
                    nlist.add(it)
                }
            }

            val filterResult = FilterResults()
            filterResult.values = nlist
            filterResult.count = nlist.size

            return filterResult
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredAppList =
                    (results!!.values as List<Searchable>?)?.sortedWith(
                            Comparator { search1, search2 ->
                        search2.searchString.toString().compareTo(
                                search1.searchString.toString(), ignoreCase = true)})
                    ?: ArrayList()
            notifyDataSetChanged()
        }

    }

    fun replaceList(searchableLists: ArrayList<List<Searchable>>) {
        this.searchableLists = searchableLists
        this.filteredAppList = searchableLists[0]
    }
}
