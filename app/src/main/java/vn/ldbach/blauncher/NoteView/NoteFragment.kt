package vn.ldbach.blauncher.NoteView

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import vn.ldbach.blauncher.R
import vn.ldbach.blauncher.ViewFragment

/**
 * This fragment is used for displaying notes
 */
class NoteFragment : ViewFragment() {
    override fun onPause() {
        noteLoader.saveNotes(notes)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        reload()
    }

    private fun reload() {
        notes = noteLoader.loadNotes()

        when (adapter) {
            null -> {
                adapter = NoteAdapter(notes, context)
                recyclerView.adapter = adapter
                Log.d("debug", "Adapter is created")
            }
            else -> {
                adapter!!.replaceList(notes)
            }
        }
    }



    private lateinit var recyclerView : RecyclerView
    private  var adapter: NoteAdapter? = null
    private lateinit var fab : FloatingActionButton
    private lateinit var noteLoader: NoteLoader

    private lateinit var notes: ArrayList<NoteData>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.viewfragment_note, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        recyclerView = view?.findViewById(R.id.list_note)!!
        fab = view.findViewById(R.id.fab_add_note)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val divider = context.getDrawable(R.drawable.note_divider)
        val noteDivider = NoteDivider(divider)

        recyclerView.addItemDecoration(noteDivider)

        noteLoader = NoteLoader(this.context)

        fab.setOnClickListener {
            _ -> run {
            // Start take note intent then save it
        }}
    }

    fun showFab() = fab.show()

    fun hideFab() = fab.hide()

    override fun onSelected() {
        showFab()
    }

    override fun onDeselected() {
        hideFab()
    }
}