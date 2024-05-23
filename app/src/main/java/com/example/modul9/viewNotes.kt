package com.example.modul9

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class viewNotes : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private var noteId: String? = null
    private lateinit var notedb: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_notes)
        val bundle: Bundle? = intent.extras

        val viewTittle: TextView = findViewById(R.id.vTittle)
        val viewDesc: TextView = findViewById(R.id.vDesc)
        val updateNote: Button = findViewById(R.id.btn_update)
        val keluar: Button = findViewById(R.id.btn_ext)
        val linear_update: LinearLayout = findViewById(R.id.lin_upt)
        val upTitle: EditText = findViewById(R.id.upt_tittle)
        val upDesc: EditText = findViewById(R.id.upt_desc)
        val btnUpdateNote: Button = findViewById(R.id.btUp)

        notedb = FirebaseDatabase.getInstance("https://prakpam-ef343-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("notes")
        noteId = bundle?.getString("id")

        viewTittle.text = bundle?.getString("judul")
        viewDesc.text = bundle?.getString("desk")

        keluar.setOnClickListener {
            finish()
        }

        updateNote.setOnClickListener {
            linear_update.visibility = View.VISIBLE
            upTitle.setText(viewTittle.text)
            upDesc.setText(viewDesc.text)
            btnUpdateNote.setOnClickListener {
                val newTitle = upTitle.text.toString()
                val newDesc = upDesc.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    if (noteId != null) {
                        notedb.child(noteId!!).child("title").setValue(newTitle).await()
                        notedb.child(noteId!!).child("description").setValue(newDesc).await()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@viewNotes, "Note updated successfully", Toast.LENGTH_SHORT).show()
                            viewTittle.text = newTitle
                            viewDesc.text = newDesc
                            linear_update.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
}