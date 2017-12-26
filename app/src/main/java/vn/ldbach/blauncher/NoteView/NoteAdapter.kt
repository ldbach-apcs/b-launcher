package vn.ldbach.blauncher.NoteView

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import vn.ldbach.blauncher.R

/**
 * Adapter and ViewHolder for note view
 */
class NoteAdapter(private var notes: ArrayList<NoteData>, private val context: Context)
    : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    fun replaceList(notes: ArrayList<NoteData>) {
        this.notes = notes
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflatedView = parent.inflate(R.layout.list_note)
        return NoteViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val itemNote = notes[position]
        holder.bind(itemNote)
    }

    override fun getItemCount() = notes.size

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachedToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachedToRoot)
    }


    class NoteViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {

        private val noteTitle = v.findViewById<TextView>(R.id.note_title)
        private val noteContent = v.findViewById<TextView>(R.id.note_content)
        private val noteIcon = v.findViewById<ImageView>(R.id.note_icon)

        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
        }


        override fun onClick(v: View?) {
            Toast.makeText(v?.context, "Clicked", Toast.LENGTH_SHORT).show()
        }

        // Show dialog for edit / delete / share
        override fun onLongClick(v: View?): Boolean {
            Toast.makeText(v?.context, "Long clicked", Toast.LENGTH_SHORT).show()
            return true
        }

        fun bind(note: NoteData) {
            noteTitle.text = note.title
            noteContent.text = note.content
        }
    }
}

