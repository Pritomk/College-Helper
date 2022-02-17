package com.example.collegehelper.volleyDao

import android.app.Application
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.collegehelper.SharedPreferenceClass
import com.example.collegehelper.repositories.StudentRepository
import com.example.collegehelper.room.student.Student
import com.example.collegehelper.room.student.StudentDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class StudentDao(val application: Application) {

    private val requestQueue = Volley.newRequestQueue(application)
    private val sharedPreferenceClass = SharedPreferenceClass(application)
    private val token = sharedPreferenceClass.getValueString("token")
    private var studentRepository: StudentRepository

    private val TAG = "com.example.collegehelper.volleyDao.StudentDao"

    init {
        val dao = StudentDatabase.getDatabase(application).getStudentDao()
        studentRepository = StudentRepository(dao)
    }

    fun insertStudentOnline(student: Student, classMongoId: String) {
        val url = "https://collegehelperapi.herokuapp.com/api/user/student"

        val map = HashMap<String, String>()
        map["studentName"] = student.studentName
        map["roll"] = student.roll.toString()
        map["classId"] = classMongoId
        Log.d(TAG, "$map")

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, JSONObject(map as Map<*, *>),{response->
            if (response.getBoolean("success")) {
                GlobalScope.launch {
                    studentRepository.insert(Student(0, student.cid, student.studentName, student.roll,
                        response.getJSONObject("student").getString("_id")))
                }
            }
        },{error->
            Log.e(TAG, "$error")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = "$token"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        requestQueue.add(jsonObjectRequest)
    }

    fun updateStudentOnline(student: Student) {
        val url = "https://collegehelperapi.herokuapp.com/api/user/student/${student.studentMongoId}"
        Log.d(TAG,url)
        val map = HashMap<String, String>()
        map["studentName"] = student.studentName
        map["roll"] = student.roll.toString()
        Log.d(TAG,"$map")
        val jsonObjectRequest = object : JsonObjectRequest(Method.PUT, url, JSONObject(map as Map<*, *>),{response->
            if (response.getBoolean("success")) {
                Log.d(TAG, "$response")
            }
        },{error->
            Log.e(TAG, "$error")
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

    fun deleteStudent(student: Student) {
        val url = "https://collegehelperapi.herokuapp.com/api/user/student/${student.studentMongoId}"

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