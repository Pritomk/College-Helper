package com.example.collegehelper.volleyDao

import android.app.Application
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.collegehelper.SharedPreferenceClass
import com.example.collegehelper.repositories.StatusRepository
import com.example.collegehelper.room.status.Status
import com.example.collegehelper.room.status.StatusDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class StatusDao(val application: Application) {

    private val requestQueue = Volley.newRequestQueue(application)
    private val sharedPreferenceClass = SharedPreferenceClass(application)
    private val token = sharedPreferenceClass.getValueString("token")
    private val statusRepository: StatusRepository
    private val TAG = "com.example.collegehelper.volleyDao.StatusDao"

    init {
        val dao = StatusDatabase.getDatabase(application).getStatusDao()
        statusRepository = StatusRepository(dao)
    }

    fun insertStatusOnline(status: Status,classMongoId: String, studentMongoId: String) {
    }

    fun updateStatusOnline(status : Status) {
    }
}