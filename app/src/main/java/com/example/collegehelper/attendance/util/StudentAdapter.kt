package com.example.collegehelper.attendance.util

import android.content.Context
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehelper.R
import com.example.collegehelper.room.status.Status
import com.example.collegehelper.room.student.Student
import android.graphics.Color

import androidx.core.content.ContextCompat

class StudentAdapter(private val listener: StudentItemClicked) :
    RecyclerView.Adapter<StudentViewHolder>() {

    private val TAG = "com.example.collegehelper.attendance.util.StudentAdapter"
    private val studentItems = ArrayList<Student>()
    private val statusItems = ArrayList<Status>()
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        val studentViewHolder = StudentViewHolder(view)
        view.setOnClickListener {
            Log.d(TAG, "${studentViewHolder.adapterPosition}")
            listener.onStudentItemClicked(statusItems[studentViewHolder.adapterPosition])
        }
        return studentViewHolder
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.recyclerName.text = studentItems[position].studentName
        holder.recyclerRoll.text = studentItems[position].roll.toString()
        holder.recyclerStatus.text = statusItems[position].status
        holder.cardView.setBackgroundColor(getColor(position))
    }

    private fun getColor(position: Int): Int {
        var status: String = statusItems[position].status
        return when (status) {
            "P" -> Color.parseColor(
                "#" + Integer.toHexString(
                    ContextCompat.getColor(
                        context,
                        R.color.present
                    )
                )
            )
            "A" -> Color.parseColor(
                "#" + Integer.toHexString(
                    ContextCompat.getColor(
                        context,
                        R.color.absent
                    )
                )
            )
            else -> Color.parseColor(
                "#" + Integer.toHexString(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return studentItems.size
    }

    fun updateList(newStudentList: ArrayList<Student>, newStatusList: ArrayList<Status>) {
        studentItems.clear()
        studentItems.addAll(newStudentList)

        statusItems.clear()
        statusItems.addAll(newStatusList)

        notifyDataSetChanged()
    }
}

class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnCreateContextMenuListener {

    val recyclerName: TextView = itemView.findViewById(R.id.recyclerName)
    val recyclerRoll: TextView = itemView.findViewById(R.id.recyclerRoll)
    val recyclerStatus: TextView = itemView.findViewById(R.id.recyclerStatus)
    val cardView: View = itemView.findViewById(R.id.cardView)

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

interface StudentItemClicked {
    fun onStudentItemClicked(statusItems: Status)
}