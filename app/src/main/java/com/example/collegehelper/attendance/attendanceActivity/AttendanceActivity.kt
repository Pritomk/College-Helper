package com.example.collegehelper.attendance.attendanceActivity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.collegehelper.R
import com.example.collegehelper.attendance.util.AttendanceAdapter
import com.example.collegehelper.attendance.util.MyCalender
import com.example.collegehelper.attendance.util.OnCalenderClickListener
import com.example.collegehelper.databinding.ActivityAttendanceBinding
import com.example.collegehelper.room.status.Status
import com.example.collegehelper.room.student.Student
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.lorentzos.flingswipe.SwipeFlingAdapterView.onFlingListener
import android.content.Intent




class AttendanceActivity : AppCompatActivity(), OnCalenderClickListener {

    private lateinit var studentItems : ArrayList<Student>
    private lateinit var statusList : ArrayList<Status>
    private lateinit var fillingAdapterView: SwipeFlingAdapterView
    private lateinit var attendanceAdapter: AttendanceAdapter
    private var cid = 0L
    private var className = ""
    private lateinit var classMongoId: String
    private var subName = ""
    private lateinit var toolBar: Toolbar
    private lateinit var binding : ActivityAttendanceBinding
    private lateinit var subTitle : TextView
    private lateinit var studentActivityViewModel: StudentActivityViewModel
    private val TAG = "com.example.collegehelper.attemdance.attendanceActivity.AttendanceActivity"
    private lateinit var calender: MyCalender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentActivityViewModel = ViewModelProvider(this,
            StudentActivityViewModelFactory(application))[StudentActivityViewModel::class.java]

        cid = intent.getLongExtra("cid",0L)
        className = intent.getStringExtra("className").toString()
        subName = intent.getStringExtra("subjectName").toString()
        classMongoId = intent.getStringExtra("classMongoId").toString()

        toolBar = findViewById(R.id.toolbar)
        toolBar.inflateMenu(R.menu.attendance_item)

        calender = MyCalender(this)

        fillingAdapterView = findViewById(R.id.frame)

        loadData()

    }

    private fun loadData() {
        studentActivityViewModel.allStudentItems.observe(this) { studentItems ->
            attendanceAdapter = AttendanceAdapter(this, studentItems as ArrayList<Student>)

            val statusList = ArrayList<Status>()
            loadStatusList(studentItems, statusList)

            var pos = 0

            fillingAdapterView.adapter = attendanceAdapter

            fillingAdapterView.setFlingListener(object : onFlingListener {
                override fun removeFirstObjectInAdapter() {
                    studentItems.removeAt(0)
                    attendanceAdapter.notifyDataSetChanged()
                }

                override fun onLeftCardExit(o: Any) {
                    statusList[pos].status = "A"
                    pos++
                }

                override fun onRightCardExit(o: Any) {
                    statusList[pos].status = "P"
                    pos++
                    makeToast(applicationContext, pos.toString() + " " + statusList.size)
                }

                override fun onAdapterAboutToEmpty(i: Int) {}
                override fun onScroll(v: Float) {}
            })

            setToolBar(statusList)

        }


    }

    private fun loadStatusList(studentItems: ArrayList<Student>, statusList: ArrayList<Status>) {
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

    private fun onMenuItemClick(menuItem: MenuItem?) : Boolean {
        if (menuItem!!.itemId == R.id.student_list) {
            openStudentActivity()
        } else if (menuItem.itemId == R.id.show_calender) {
            showCalender()
        }
        return true
    }

    private fun showCalender() {
        calender.show(supportFragmentManager,"")
    }

    private fun openStudentActivity() {
        val intent = Intent(this, StudentActivity::class.java)
        intent.putExtra("className", className)
        intent.putExtra("subjectName", subName)
        intent.putExtra("cid", cid)
        intent.putExtra("classMongoId", classMongoId)

        startActivity(intent)
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

    private fun makeToast(ctx: Context, text: String) {
        Toast.makeText(ctx,text,Toast.LENGTH_SHORT).show()
    }

    override fun onCalenderClicked(year: Int, month: Int, day: Int) {
        calender.setDate(year, month, day)
        subTitle.text = subName+" | "+calender.getDate()
        Toast.makeText(this,calender.getDate(),Toast.LENGTH_SHORT).show()
    }
}