package com.example.collegehelper.attendance.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.collegehelper.room.student.Student
import com.example.collegehelper.R



class AttendanceAdapter(private val context: Context,private val studentItems: ArrayList<Student>) : BaseAdapter() {

    private lateinit var frameNameTxt : TextView
    private lateinit var frameRollTxt : TextView

    override fun getCount(): Int {
        return studentItems.size
    }

    override fun getItem(p0: Int): Any {
        return 0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_item_layout, parent, false)
        }
        frameNameTxt = convertView!!.findViewById(R.id.frameNameTxt)
        frameRollTxt = convertView.findViewById(R.id.frameRollTxt)
        frameNameTxt.text = studentItems[position].studentName
        frameRollTxt.text = studentItems[position].roll.toString()

        return convertView
    }

}