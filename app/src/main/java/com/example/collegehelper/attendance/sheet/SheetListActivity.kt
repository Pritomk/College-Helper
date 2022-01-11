package com.example.collegehelper.attendance.sheet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.collegehelper.R
import com.example.collegehelper.attendance.attendanceActivity.StudentActivityViewModel
import com.example.collegehelper.attendance.attendanceActivity.StudentActivityViewModelFactory
import com.example.collegehelper.attendance.util.OnCalenderClickListener
import com.example.collegehelper.databinding.ActivitySheetListBinding
import com.example.collegehelper.room.status.Status
import kotlin.collections.ArrayList
import android.widget.ArrayAdapter as ArrayAdapter

class SheetListActivity : AppCompatActivity(), OnCalenderClickListener {

    private lateinit var binding: ActivitySheetListBinding
    private lateinit var className: String
    private lateinit var subName: String
    private var cid = 0L
    private lateinit var sheetList: ListView
    private lateinit var studentActivityViewModel: StudentActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySheetListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        className = intent.getStringExtra("className").toString()
        subName = intent.getStringExtra("subName").toString()
        cid = intent.getLongExtra("cid", 0L)

        sheetList = binding.sheetList

        studentActivityViewModel = ViewModelProvider(
            this,
            StudentActivityViewModelFactory(application)
        )[StudentActivityViewModel::class.java]

        loadStatusList()
    }

    private fun loadStatusList() {
        studentActivityViewModel.getAllStatus(cid).observe(this, { statusList ->
            val dates: ArrayList<String> = ArrayList()
            loadMonths(dates, statusList)

            val adapter = ArrayAdapter(this, R.layout.sheet_list_item, R.id.date_list_item, dates)
            sheetList.adapter = adapter

            sheetList.onItemClickListener =
                AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                    openSheetActivity(
                        position,statusList
                    )
                }
        })
    }

    private fun openSheetActivity(position: Int, statusList: List<Status>) {
        val intent = Intent(this,SheetActivity::class.java)
        intent.putExtra("className",className)
        intent.putExtra("subName",subName)
        intent.putExtra("cid",cid)
        intent.putExtra("month",statusList[position].dateKey.substring(3))
        startActivity(intent)
    }

    private fun loadMonths(dates: ArrayList<String>, statusList: List<Status>) {
        for (status in statusList) {
            if (!dates.contains(status.dateKey.substring(3))) {
                dates.add(status.dateKey.substring(3))
            }
        }
    }

    override fun onCalenderClicked(year: Int, month: Int, day: Int) {

    }
}