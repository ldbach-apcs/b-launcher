package vn.ldbach.blauncher.NoteView

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vn.ldbach.blauncher.R
import vn.ldbach.blauncher.ViewFragment

/**
 * This fragment is used for displaying notes
 */
class NoteFragment : ViewFragment() {
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    private lateinit var recyclerView : RecyclerView
    private lateinit var fab : FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.viewfragment_note, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        recyclerView = view?.findViewById(R.id.list_note)!!
        fab = view.findViewById(R.id.fab_add_note)

        fab.setOnClickListener {
            _ -> run {
            // Start take note intent then save it
        }}
    }
}