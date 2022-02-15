package com.example.collegehelper.volleyDao

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.lifecycle.ViewModelProvider
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.collegehelper.SharedPreferenceClass
import com.example.collegehelper.repositories.ClassItemRepository
import com.example.collegehelper.room.classItem.ClassItem
import com.example.collegehelper.room.classItem.ClassItemDatabase
import com.example.collegehelper.ui.attendance.AttendanceViewModel
import com.example.collegehelper.ui.attendance.AttendanceViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class ClassDao(val application: Application) {

    private val requestQueue = Volley.newRequestQueue(application)
    private val sharedPreferenceClass = SharedPreferenceClass(application)
    private val token = sharedPreferenceClass.getValueString("token")
    private val TAG = "com.example.collegehelper.volleyDao.ClassDao"
    private lateinit var classItemRepository: ClassItemRepository

    init {
        val dao = ClassItemDatabase.getDatabase(application).getClassItemDao()
        classItemRepository = ClassItemRepository(dao)
    }

    fun insertClass(className: String, subName: String) {
        val url = "https://collegehelperapi.herokuapp.com/api/user/class"

        val map = HashMap<String, String>()
        map["className"] = className
        map["subName"] = subName
        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, JSONObject(map as Map<*, *>), {response ->
            if (response.getBoolean("success")) {
                Log.d(TAG, "$response")
                GlobalScope.launch {
                    classItemRepository.insertClassItem(ClassItem(0,className,subName,response.getJSONObject("classVar").getString("_id")))
                }
            }
        },{ error ->
            Log.e(TAG, "${error.message}")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "$token"
                return params
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        requestQueue.add(jsonObjectRequest)
    }

    fun updateClass(className: String, subName: String,mongoId: String) {
        val url = "https://collegehelperapi.herokuapp.com/api/user/class/$mongoId"

        val map = HashMap<String, String>()
        map["className"] = className
        map["subName"] = subName
        val jsonObjectRequest = object : JsonObjectRequest(Method.PUT, url, JSONObject(map as Map<*, *>), {response ->
            if (response.getBoolean("success")) {
                Log.d(TAG, "$response")
            }
        },{ error ->
            Log.e(TAG, "${error.message}")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "$token"
                return params
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        requestQueue.add(jsonObjectRequest)
    }

    fun deleteClassItem(mongoId: String) {
        val url = "https://collegehelperapi.herokuapp.com/api/user/class/$mongoId"

        val jsonObjectRequest = object : JsonObjectRequest(Method.DELETE, url, null, {response ->
            if (response.getBoolean("success")) {
                Log.d(TAG, "$response")
            }
        },{ error ->
            Log.e(TAG, "${error.message}")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "$token"
                return params
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        requestQueue.add(jsonObjectRequest)
    }


}