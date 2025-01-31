package com.crocodic.yourjourney

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crocodic.yourjourney.model.Note
import java.text.SimpleDateFormat
import java.util.*

class EditJourney : AppCompatActivity() {
    private var saveDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_journey)

        val note = intent.getParcelableExtra<Note>("note")

        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val noteEditText = findViewById<EditText>(R.id.noteEditText)
        val dateTimeTextView = findViewById<TextView>(R.id.dateTimeTextView)

        note?.let {
            titleEditText.setText(it.title)
            noteEditText.setText(it.content)
            dateTimeTextView.text = it.dateTime
        }

        val saveButton = findViewById<ImageButton>(R.id.saveButton)
        saveButton.setOnClickListener {
            val dialog = Dialog(this)

            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

            val view = LayoutInflater.from(this).inflate(R.layout.dialog_save_confirmation, null)
            dialog.setContentView(view)

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val cancelButton = view.findViewById<Button>(R.id.cancelButton)
            val saveButton = view.findViewById<Button>(R.id.saveButton)

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            saveButton.setOnClickListener {
                val titleEditText = findViewById<TextView>(R.id.titleEditText)
                val noteEditText = findViewById<TextView>(R.id.noteEditText)

                if (titleEditText.text.isBlank() || noteEditText.text.isBlank()) {
                    Toast.makeText(this, "Title and Content cannot be empty!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val updatedNote = note?.copy(
                    title = titleEditText.text.toString(),
                    content = noteEditText.text.toString(),
                    dateTime = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault()).format(Date())
                )

                val resultIntent = Intent()
                resultIntent.putExtra("note", updatedNote)
                setResult(RESULT_OK, resultIntent)
                finish()



                saveDialog = Dialog(this)
                finish()
            }

            dialog.show()
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
    override fun onDestroy() {
        saveDialog?.dismiss()
        super.onDestroy()
    }

}
