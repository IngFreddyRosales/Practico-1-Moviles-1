package com.example.practico1.ComponentLayout

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.practico1.ItemNote.Note
import com.example.practico1.R
import com.example.practico1.databinding.ActivityNoteLayoutBinding

class NoteAdapter(
    private val notesList: MutableList<Note> = mutableListOf(),
    private val listener: OnNoteClickListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ActivityNoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notesList[position], listener)
    }

    fun itemDelete(note: Note) {
        val index = notesList.indexOf(note)
        if (index > -1) {
            notesList.removeAt(index)
            notifyItemRemoved(index) //aquiel notifyItemRemoved notifica al adaptador que elimino un elemento en una posición específica
        }

    }

    fun itemEdit(note: Note) {
        val index = notesList.indexOf(note)
        notesList[index] = note
        notifyItemChanged(index) //aquí notifyItemChanged notifica al adaptador que un elemento ha cambiado en una posición específica
    }

    class NoteViewHolder(private val binding: ActivityNoteLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(note: Note, listener: OnNoteClickListener) {
            binding.noteTitle.text = note.title
            binding.noteDescripcion.text = note.description
            binding.root.setBackgroundColor(note.color) // Cambio de color del fonde del item nota

            binding.btnUpdate.setOnClickListener {
                listener.onNoteEditClickListener(note)
            }
            binding.btnDelete.setOnClickListener {
                listener.onNoteDeleteClickListener(note)
            }

        }

    }

    public interface OnNoteClickListener {
        fun onNoteEditClickListener(note: Note)
        fun onNoteDeleteClickListener(note: Note)
    }

}