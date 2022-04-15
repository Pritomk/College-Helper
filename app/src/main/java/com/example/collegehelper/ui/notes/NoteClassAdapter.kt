package com.example.collegehelper.ui.notes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehelper.R
import com.example.collegehelper.room.notes.NoteClass

class NoteClassAdapter(private val listener: NoteClassItemClicked) : RecyclerView.Adapter<NoteClassViewHolder>() {

    private val classItems : ArrayList<NoteClass> = ArrayList()
    private val TAG = "com.example.collegehelper.ui.notes.NoteClassAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_list_item, parent, false)
        val viewHolder = NoteClassViewHolder(view)

        view.setOnClickListener {
            listener.onNoteClassItemClicked(classItems[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteClassViewHolder, position: Int) {
        val noteClassItem = classItems[holder.adapterPosition]
        holder.className.text = noteClassItem.className
        holder.subName.text = noteClassItem.subName
    }

    override fun getItemCount(): Int {
        return classItems.size
    }

    fun updateClassItems(newList: ArrayList<NoteClass>) {
        classItems.clear()
        classItems.addAll(newList)

        notifyDataSetChanged()
    }


}

class NoteClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val className: TextView = itemView.findViewById(R.id.note_class_tv)
    val subName: TextView = itemView.findViewById(R.id.note_subject_tv)
}

interface NoteClassItemClicked {
    fun onNoteClassItemClicked(noteClass: NoteClass)
}