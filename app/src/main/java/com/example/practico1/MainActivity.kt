package com.example.practico1

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practico1.ItemNote.Note
import com.example.practico1.databinding.ActivityMainBinding
import com.example.practico1.ComponentLayout.NoteAdapter
import com.example.practico1.databinding.DialogAddNoteBinding

class MainActivity : AppCompatActivity(), NoteAdapter.OnNoteClickListener {

    private lateinit var binding: ActivityMainBinding
    private val notes: MutableList<Note> = mutableListOf(
        Note(title = "Actividades deportivas", description = "Ir a correr con los amigos a las 6 AM", color = Color.WHITE),
        Note(title = "Tareas", description = "Clases de programacion a las 10 AM", color = Color.RED),
        Note(title = "Actividades recreativas", description = "Salir a comer con Alejandra el Domingo a las 6 PM", color = Color.GREEN),
        Note(title = "Tareas", description = "Terminar el proyecto de programacion", color = Color.BLUE),
    )

    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)){
            v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/ // No se necesita ya que se implemento la funcion enableEdgeToEdge
        //java.lang.NullPointerException: Attempt to invoke virtual method 'void android.view.View.setOnApplyWindowInsetsListener(android.view.View$OnApplyWindowInsetsListener)' on a null object reference

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteAdapter = NoteAdapter(notes, this)
        setupNotesRyclerView()

        binding.floatingActionButton2.setOnClickListener {
            showAddNoteDialog()
        }

    }
    private fun setupNotesRyclerView(){
        binding.rvItemNotes.adapter = noteAdapter
        binding.rvItemNotes.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
    }

    private fun showAddNoteDialog(note: Note? = null) {
        val binding = DialogAddNoteBinding.inflate(layoutInflater)
        var selectedColor = Color.WHITE

        val colorButtons = listOf(
            binding.btnWhite to Color.WHITE,
            binding.btnRed to Color.RED,
            binding.btnGreen to Color.GREEN,
            binding.btnBlue to Color.BLUE,
            binding.btnYellow to Color.YELLOW,
            binding.btnOrange to Color.CYAN,
            binding.btnPink to Color.MAGENTA,
            binding.btnPurple to Color.GRAY,
            binding.btnNavyBlue to Color.BLACK,
            binding.btnTurquoise to Color.LTGRAY
        )

        colorButtons.forEach { (button, color) ->
            button.setOnClickListener {
                selectedColor = color
            }
        }

        // ? Llamada segura
        binding.editTextTitle.setText(note?.title)
        binding.editTextDescription.setText(note?.description)

        // Crear el diálogo con el layout inflado
        val dialog = AlertDialog.Builder(this)
            .setTitle(if (note != null) "Edit Note" else "Add Note")
            .setView(binding.root)
            .setNegativeButton("Cancel", null)
            .create()

        // Guardar o editar la nota al hacer clic en el botón de guardado
        binding.btnSaveItem.setOnClickListener {
            if (binding.editTextTitle.text.isEmpty() || binding.editTextDescription.text.isEmpty()) {
                Toast.makeText(this, "El título y la descripción no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            val newNote = Note(
                title = title,
                description = description,
                color = selectedColor
            )

            // Si estamos editando una nota existente
            if (note != null) {
                note.title = title
                note.description = description
                note.color = selectedColor
                editNoteFromList(note)
            } else {
                // Si estamos añadiendo una nueva nota
                addNote(newNote)
            }

            dialog.dismiss()
        }
        dialog.show()
    }


    private fun addNote(newNote: Note) {
        notes.add(newNote)
        noteAdapter.notifyItemInserted(notes.size - 1 ) // Actualizar la lista de notas
    }

    private fun editNoteFromList(note: Note){
        noteAdapter.itemEdit(note)
    }

    override fun onNoteEditClickListener(note: Note) {
        showAddNoteDialog(note)
    }

    override fun onNoteDeleteClickListener(note: Note) {
        noteAdapter.itemDelete(note)
    }
}