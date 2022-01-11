package com.example.collegehelper.ui.attendance

import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehelper.R
import com.example.collegehelper.room.classItem.ClassItem

class ClassAdapter(private val listener: ClassItemClicked) : RecyclerView.Adapter<ClassItemViewHolder>(){

    private val classItems : ArrayList<ClassItem> = ArrayList()
    private val TAG = "com.example.collegehelper.ui.attendance.ClassAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassItemViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.class_item_layout,parent,false)
        val viewHolder = ClassItemViewHolder(view)

        view.setOnClickListener {
            Log.d(TAG,"${viewHolder.adapterPosition}")
            listener.onClassItemClicked(classItems[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ClassItemViewHolder, position: Int) {
        val classItem = classItems[holder.adapterPosition]
        holder.className.text = classItem.className
        holder.subName.text = classItem.subjectName
    }

    override fun getItemCount(): Int {
        return classItems.size
    }

    fun updateClassItems(newClassItems: ArrayList<ClassItem>) {
        classItems.clear()
        classItems.addAll(newClassItems)

        notifyDataSetChanged()
    }


}

class ClassItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnCreateContextMenuListener{
    val className : TextView = itemView.findViewById(R.id.class_tv)
    val subName : TextView = itemView.findViewById(R.id.subject_tv)

    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(
        p0: ContextMenu?,
        p1: View?,
        p2: ContextMenu.ContextMenuInfo?
    ) {
        p0?.add(adapterPosition,0,0,"Edit")
        p0?.add(adapterPosition,1,0,"Delete")
    }
}

interface ClassItemClicked {
    fun onClassItemClicked(item: ClassItem)
}