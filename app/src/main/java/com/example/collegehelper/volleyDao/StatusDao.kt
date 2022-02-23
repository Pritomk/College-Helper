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
        val url = "https://collegehelperapi.herokuapp.com/api/user/status"

        val map = HashMap<String, String>()
        map["status"] = status.status
        map["date"] = status.dateKey
        map["classId"] = classMongoId
        map["studentId"] = studentMongoId

        val jsonObjectRequest : JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, JSONObject(
            map as Map<*, *>
        ), { response ->
            Log.d(TAG, "$response")
            status.statusMongoId = response.getJSONObject("status").getString("_id")
            GlobalScope.launch {
                statusRepository.insert(status)
            }
        }, { error ->
            Log.e(TAG, "$error : ${error.message}")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "$token"
                return params
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        requestQueue.add(jsonObjectRequest)
    }

    fun updateStatusOnline(status : Status) {
        val url = "https://collegehelperapi.herokuapp.com/api/user/status/${status.statusMongoId}"

        val map = HashMap<String, String>()
        map["status"] = status.status

        val jsonObjectRequest = object : JsonObjectRequest(Method.PUT, url, JSONObject(map as Map<*, *>), { response ->
            if (response.getBoolean("success")) {
                Log.d(TAG, "$response")
            }
        },{ error ->
            Log.e(TAG, "$error : ${error.message}")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "$token"
                return params
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        requestQueue.add(jsonObjectRequest)
    }
}