package com.example.collegehelper.attendance.sheet

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import androidx.lifecycle.ViewModelProvider
import com.example.collegehelper.R
import com.example.collegehelper.attendance.attendanceActivity.StudentActivityViewModel
import com.example.collegehelper.attendance.attendanceActivity.StudentActivityViewModelFactory
import com.example.collegehelper.databinding.ActivitySheetBinding
import com.example.collegehelper.room.student.Student
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates
import android.widget.TextView
import android.graphics.Typeface
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.YearMonth


class SheetActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySheetBinding
    private lateinit var tableLayout: TableLayout
    private lateinit var studentActivityViewModel: StudentActivityViewModel
    private lateinit var className: String
    private lateinit var subName: String
    private var cid = 0L
    private lateinit var month: String
    private val TAG = "com.example.collegehelper.attendance.sheet.SheetActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tableLayout = binding.tableLayout

        className = intent.getStringExtra("className").toString()
        subName = intent.getStringExtra("subName").toString()
        cid = intent.getLongExtra("cid",0L)
        month = intent.getStringExtra("month").toString()
        studentActivityViewModel = ViewModelProvider(this,
            StudentActivityViewModelFactory(application))[StudentActivityViewModel::class.java]

        loadData()
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {
        studentActivityViewModel.getClassStudents(cid).observe(this,{ studentItems ->
            val idList : ArrayList<Long> = ArrayList()
            val nameList = ArrayList<String>()
            val rollList = ArrayList<Int>()
            loadListData(studentItems,idList,nameList,rollList)

            val DAY_IN_MONTH = getDayInMonth(month)
            Log.d(TAG,"$DAY_IN_MONTH")

            val rowSize = idList.size+1;

            val rows: Array<TableRow?> = arrayOfNulls(rowSize)
            val roll_tvs = arrayOfNulls<TextView>(rowSize)
            val name_tvs = arrayOfNulls<TextView>(rowSize)
            val status_tvs = Array(rowSize) {
                arrayOfNulls<TextView>(
                    DAY_IN_MONTH + 1
                )
            }


            for (i in 0 until rowSize) {
                roll_tvs[i] = TextView(this)
                name_tvs[i] = TextView(this)
                for (j in 1..DAY_IN_MONTH) {
                    status_tvs[i][j] = TextView(this)
                }
            }

            //header

            roll_tvs[0]!!.text = "Roll"
            roll_tvs[0]!!.setTypeface(roll_tvs[0]!!.typeface, Typeface.BOLD)
            name_tvs[0]!!.text = "Name"
            name_tvs[0]!!.setTypeface(name_tvs[0]!!.typeface, Typeface.BOLD)
            for (i in 1..DAY_IN_MONTH) {
                status_tvs[0][i]!!.text = i.toString() + ""
                status_tvs[0][i]!!.setTypeface(status_tvs[0][i]!!.typeface, Typeface.BOLD)
            }

            for (i in 1 until rowSize) {
                roll_tvs[i]!!.text = rollList[i - 1].toString() + ""
                name_tvs[i]!!.text = nameList[i-1] + ""
                for (j in 1..DAY_IN_MONTH) {
                    var day = j.toString()
                    if (day.length == 1) day = "0$day"
                    val date = "$day.$month"
                    studentActivityViewModel.getStatus(cid,idList[i-1],date).observe(this,{status->
                        status_tvs[i][j]!!.text = status
                    })
                }
            }

            for (i in 0 until rowSize) {
                rows[i] = TableRow(this)
                if (i % 2 == 0) {
                    rows[i]!!.setBackgroundColor(Color.parseColor("#68EBC9"))
                } else {
                    rows[i]!!.setBackgroundColor(Color.parseColor("#F1959B"))
                }
                roll_tvs[i]!!.setPadding(16, 16, 16, 16)
                name_tvs[i]!!.setPadding(16, 16, 16, 16)
                rows[i]!!.addView(roll_tvs[i])
                rows[i]!!.addView(name_tvs[i])
                for (j in 1..DAY_IN_MONTH) {
                    status_tvs[i][j]!!.setPadding(16, 16, 16, 16)
                    rows[i]!!.addView(status_tvs[i][j])
                }
                tableLayout.addView(rows[i])
            }
            tableLayout.showDividers = TableLayout.SHOW_DIVIDER_MIDDLE;

        })
    }

    private fun loadListData(studentItems: List<Student>?,
                             idList: ArrayList<Long>, nameList: ArrayList<String>, rollList: ArrayList<Int>) {
        for (item in studentItems!!) {
            idList.add(item.sid)
            nameList.add(item.studentName)
            rollList.add(item.roll)
        }
    }

    private fun getDayInMonth(month: String): Int {
        val monthIndex = Integer.valueOf(month.substring(0, 1))
        val year = Integer.valueOf(month.substring(4))
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, monthIndex)
        calendar.set(Calendar.YEAR, year)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}