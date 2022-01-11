package com.example.collegehelper.attendance.attendanceActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehelper.R
import com.example.collegehelper.attendance.sheet.SheetListActivity
import com.example.collegehelper.attendance.util.MyCalender
import com.example.collegehelper.attendance.util.OnCalenderClickListener
import com.example.collegehelper.attendance.util.StudentAdapter
import com.example.collegehelper.attendance.util.StudentItemClicked
import com.example.collegehelper.databinding.ActivityStudentBinding
import com.example.collegehelper.room.status.Status
import com.example.collegehelper.room.student.Student
import com.example.collegehelper.ui.dialog.AddButtonClicked
import com.example.collegehelper.ui.dialog.MyDialog

class StudentActivity : AppCompatActivity(), OnCalenderClickListener, StudentItemClicked,
    AddButtonClicked {

    private lateinit var calender: MyCalender
    private lateinit var className: String
    private lateinit var subName: String
    private var cid = 0L
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityStudentBinding
    private lateinit var studentActivityViewModel: StudentActivityViewModel
    private lateinit var title: TextView
    private lateinit var toolBar: Toolbar
    private lateinit var subTitle: TextView
    private lateinit var dialog: MyDialog
    private val TAG = "com.example.collegehelper.attendance.attendanceActivity.StudentActivity"
    private lateinit var adapter: StudentAdapter
    private lateinit var studentList: List<Student>
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calender = MyCalender(this)
        className = intent.getStringExtra("className").toString()
        subName = intent.getStringExtra("subName").toString()
        cid = intent.getLongExtra("cid",0L)
        dialog = MyDialog(this)

        toolBar = findViewById(R.id.toolbar)
        toolBar.inflateMenu(R.menu.student_item)

        recyclerView = binding.studentRecycler

        studentActivityViewModel = ViewModelProvider(this,
            StudentActivityViewModelFactory(application))[StudentActivityViewModel::class.java]


        loadData()
    }

    private fun loadData() {
        adapter = StudentAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        studentActivityViewModel.allStudentItems.observe(this,{ studentItems->
            studentList = studentItems
            val statusList = ArrayList<Status>()
            loadStatusList(studentItems,statusList)

            adapter.updateList(studentItems as ArrayList<Student>,statusList)

            setToolBar(statusList)
        })
    }

    private fun loadStatusList(studentItems: List<Student>, statusList: ArrayList<Status>) {
        for (studentItem in studentItems) {
            statusList.add(Status(0,studentItem.sid,studentItem.cid,"",""))
        }
    }

    private fun setToolBar(statusList: ArrayList<Status>) {
        subTitle = findViewById(R.id.subtitle_toolbar)
        val title: TextView = findViewById(R.id.title_toolbar)
        val back: ImageButton = findViewById(R.id.backButton)
        val save: ImageButton = findViewById(R.id.iconSave)

        title.text = className
        subTitle.text = subName+" | "+calender.getDate()

        save.setOnClickListener { saveStatus(statusList) }
        back.setOnClickListener { onBackPressed() }

        toolBar.setOnMenuItemClickListener { menuItem -> onMenuItemClick(menuItem) }
    }

    private fun saveStatus(statusList: ArrayList<Status>) {
        Toast.makeText(this,calender.getDate(),Toast.LENGTH_SHORT).show()

        for (item in statusList) {
            var status = item.status
            if (status != "P")
                status = "A"
            studentActivityViewModel.insertStatus(Status(0,item.sid,item.cid,status,calender.getDate()!!))
        }

    }

    private fun onMenuItemClick(menuItem: MenuItem?) : Boolean {
        when(menuItem!!.itemId) {
             R.id.add_student -> {
                showAddStudentDialog()
            }
            R.id.show_calender -> {
                showCalender()
            }
            R.id.show_attendance_sheet -> {
                openSheetListActivity()
            }
        }

        return true
    }

    private fun openSheetListActivity() {
        val intent = Intent(this,SheetListActivity::class.java)
        intent.putExtra("className",className)
        intent.putExtra("subName",subName)
        intent.putExtra("cid",cid)
        startActivity(intent)
    }

    private fun showAddStudentDialog() {
        dialog.show(supportFragmentManager,dialog.STUDENT_ADD_DIALOG)
    }

    private fun showCalender() {
        calender.show(supportFragmentManager,"")
    }

    override fun onCalenderClicked(year: Int, month: Int, day: Int) {
        calender.setDate(year, month, day)
        subTitle.text = subName+" | "+calender.getDate()
        Toast.makeText(this,calender.getDate(), Toast.LENGTH_SHORT).show()
    }

    override fun onStudentItemClicked(statusItem: Status) {
        changeStatus(statusItem)
    }

    private fun changeStatus(statusItem: Status) {
        var status: String = statusItem.status
        statusItem.status = if (status == "P") "A" else "P"
        adapter.notifyDataSetChanged()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            0 -> {
                position = item.groupId
                showUpdateDialog()
            }
            1 -> {
                deleteStudent(item.groupId)
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun showUpdateDialog() {
        dialog.show(supportFragmentManager,dialog.STUDENT_UPDATE_DIALOG)
    }

    private fun deleteStudent(groupId: Int) {
        studentActivityViewModel.deleteStudent(studentList[groupId])
    }

    override fun onAddClicked(text01: String, text02: String) {
        studentActivityViewModel.insertStudent(Student(0,cid,text01,text02.toInt()))
    }

    override fun onUpdateClicked(text01: String, text02: String) {
        Log.d(TAG,"$position  $studentList")
        studentActivityViewModel.updateStudent(Student(studentList[position].sid,cid,text01,text02.toInt()))
    }


}