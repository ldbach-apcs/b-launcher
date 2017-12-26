package vn.ldbach.blauncher.NoteView

import android.content.Context
import android.util.Log

/**
 * Responsible for saving and loading notes
 */
class NoteLoader(private val context: Context) {
    fun loadNotes(): ArrayList<NoteData> {
        val loaded = ArrayList<NoteData>()

        // Testing content overflow
        loaded.add(NoteData("First note",
                "I am very happy to write my first note here to test " +
                "the capability of my recycler view"))
        loaded.add(NoteData("Second note",
                "I am very happy to write my second note here to test " +
                "the capability of my recycler view"))
        loaded.add(NoteData("Third note",
                "I am very happy to write my third note here to test " +
                "the capability of my recycler view"))
        loaded.add(NoteData("Fourth note",
                "I am very happy to write my fourth note here to test " +
                "the capability of my recycler view"))
        loaded.add(NoteData("Fifth note",
                "I am very happy to write my fifth note here to test " +
                "the capability of my recycler view"))

        // Testing title overflow
        loaded.add(NoteData("First note with a lengthy title to test overflow",
                "Long content tested, only now test the title"))
        loaded.add(NoteData("Second note with a lengthy title to test overflow",
                "Long content tested, only now test the title"))

        // Testing scrolling
        loaded.add(NoteData("More note for scrolling ei?",
                "Scrolling is fun fun"))
        loaded.add(NoteData("More note for scrolling ei?",
                "Scrolling is fun fun"))
        loaded.add(NoteData("More note for scrolling ei?",
                "Scrolling is fun fun"))
        loaded.add(NoteData("More note for scrolling ei?",
                "Scrolling is fun fun"))
        loaded.add(NoteData("More note for scrolling ei?",
                "Scrolling is fun fun"))
        loaded.add(NoteData("More note for scrolling ei?",
                "Scrolling is fun fun"))


        Log.d("debug", "Notes size ${loaded.size}")
        return loaded
    }

    fun saveNotes(notes: ArrayList<NoteData>) {}
}