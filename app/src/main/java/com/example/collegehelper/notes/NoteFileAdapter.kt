package com.example.collegehelper.notes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehelper.R
import com.example.collegehelper.room.noteFile.NoteFile

class NoteFileAdapter(private val listener: OnNoteFileClickListener) : RecyclerView.Adapter<NoteFileViewHolder>() {

    private val noteFiles: ArrayList<NoteFile> = ArrayList()

    private val TAG = "com.example.collegehelper.notes.NoteFileAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteFileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_file_item, parent, false)
        val viewHolder = NoteFileViewHolder(view)

        view.setOnClickListener {
            listener.onNoteFileItemClicked(noteFiles[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteFileViewHolder, position: Int) {
        Log.d(TAG, "${noteFiles[position ]}")
        holder.pdfFileName.text = noteFiles[position].fileName

    }

    override fun getItemCount(): Int {
        return noteFiles.size
    }

    fun updateList(newList: ArrayList<NoteFile>) {
        noteFiles.clear()
        noteFiles.addAll(newList)

        notifyDataSetChanged()
    }

}

class NoteFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val pdfFileName: TextView = itemView.findViewById(R.id.pdf_file_tv)
}

interface OnNoteFileClickListener {
    fun onNoteFileItemClicked(noteFile: NoteFile)
}