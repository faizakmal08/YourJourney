    package com.crocodic.yourjourney

    import android.content.Intent
    import android.icu.text.SimpleDateFormat
    import android.os.Bundle
    import android.os.Parcelable
    import android.view.View
    import android.widget.ImageButton
    import android.widget.LinearLayout
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.GridLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.crocodic.yourjourney.model.Note
    import java.util.Date
    import java.util.Locale


    class MainActivity : AppCompatActivity() {
        private val notesList = mutableListOf<Note>()
        private lateinit var notesAdapter: NotesAdapter
        private lateinit var recyclerView: RecyclerView
        private lateinit var contentSection: LinearLayout


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val fab = findViewById<ImageButton>(R.id.fab)
            recyclerView = findViewById(R.id.recyclerView)
            contentSection = findViewById(R.id.content)
            recyclerView.layoutManager = GridLayoutManager(this, 2)
            // Set up RecyclerView
            notesAdapter = NotesAdapter(notesList) { note ->
                val intent = Intent(this, EditJourney::class.java)
                intent.putExtra("note", note as Parcelable)
                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE) // Benar
            }



            recyclerView.adapter = notesAdapter


            // Add new note
            fab.setOnClickListener {
                // Buat ID unik untuk catatan baru
                val newId = if (notesList.isNotEmpty()) notesList.maxOf { it.id } + 1 else 1

                // Buat objek catatan baru dengan ID baru
                val newNote = Note(
                    id = newId,
                    title = "", // Kosongkan karena catatan baru
                    content = "", // Kosongkan karena catatan baru
                    dateTime = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(Date())
                )

                // Kirim catatan baru ke NewJourney untuk diisi
                val intent = Intent(this, NewJourney::class.java)
                intent.putExtra("note", newNote)
                startActivityForResult(intent, REQUEST_CODE_ADD_NOTE)
            }



            updateUI()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == REQUEST_CODE_EDIT_NOTE && resultCode == RESULT_OK) {
                val updatedNote = data?.getParcelableExtra<Note>("note")
                updatedNote?.let { note ->
                    val index = notesList.indexOfFirst { it.id == note.id }
                    if (index != -1) {
                        notesList[index] = note
                        notesAdapter.notifyItemChanged(index)
                    }
                    updateUI()
                }
            }

            if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
                val newNote = data?.getParcelableExtra<Note>("note")
                newNote?.let {
                    notesList.add(it)
                    notesAdapter.notifyItemInserted(notesList.size - 1)
                    updateUI()
                }
            }
        }







        private fun updateUI() {
            contentSection.visibility = if (notesList.isEmpty()) View.VISIBLE else View.GONE
            recyclerView.visibility = if (notesList.isEmpty()) View.GONE else View.VISIBLE
        }


        companion object {
            const val REQUEST_CODE_ADD_NOTE = 1
            const val REQUEST_CODE_EDIT_NOTE = 2 // Tambahkan di sini
        }

    }
