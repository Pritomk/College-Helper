package com.example.collegehelper.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.ServerError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.collegehelper.MainActivity
import com.example.collegehelper.SharedPreferenceClass
import com.example.collegehelper.attendance.attendanceActivity.StudentActivityViewModel
import com.example.collegehelper.attendance.attendanceActivity.StudentActivityViewModelFactory
import com.example.collegehelper.databinding.ActivityLoginBinding
import com.example.collegehelper.room.classItem.ClassItem
import com.example.collegehelper.room.status.Status
import com.example.collegehelper.room.student.Student
import com.example.collegehelper.ui.attendance.AttendanceViewModel
import com.example.collegehelper.ui.attendance.AttendanceViewModelFactory
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailText: EditText
    private lateinit var passText: EditText
    private lateinit var loginBtn: TextView
    private lateinit var regBtn: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferenceClass: SharedPreferenceClass
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var studentActivityViewModel: StudentActivityViewModel
    private val TAG = "com.example.collegehelper.authentication.LoginActivity"
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferenceClass = SharedPreferenceClass(this)

        emailText = binding.emailET
        passText = binding.passwordET
        loginBtn = binding.btnLogin
        regBtn = binding.createAccountBtn
        progressBar = binding.progressBar

        requestQueue = Volley.newRequestQueue(this)

        studentActivityViewModel = ViewModelProvider(
            this,
            StudentActivityViewModelFactory(application)
        )[StudentActivityViewModel::class.java]
        attendanceViewModel = ViewModelProvider(
            this,
            AttendanceViewModelFactory(application)
        )[AttendanceViewModel::class.java]

        regBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginBtn.setOnClickListener {
            thread.start()
            loginFunc()
        }

        binding.googleLogin.setOnClickListener {
            startActivity(Intent(this, GoogleSignInActivity::class.java))
        }
    }

    private fun loginFunc() {
        progressBar.visibility = View.VISIBLE
        val email = emailText.text.toString()
        val password = passText.text.toString()

        val url = "https://collegehelperapi.herokuapp.com/api/user/auth/login"

        val map = HashMap<String, String>()
        map["email"] = email
        map["password"] = password

        val jsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, JSONObject(map as Map<*, *>), { response ->
                try {
                    if (response.getBoolean("success")) {
                        val token = response.getString("token")
                        sharedPreferenceClass.setValueString("token", token)
                        loadData(token)
                        progressBar.visibility = View.GONE
                    }
                } catch (e: JSONException) {
                    progressBar.visibility = View.GONE
                    e.printStackTrace()
                }
            }, { error ->
                val response = error.networkResponse
                if (error != null && error is ServerError) {
                    try {
                        val res = String(response.data)
                        val obj = JSONObject(res)
                        Toast.makeText(this, "Error is ${obj.getString("msg")}", Toast.LENGTH_SHORT)
                            .show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                progressBar.visibility = View.GONE
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["Content-Type"] = "application/json"
                    return params
                }
            }

        val policy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jsonObjectRequest.retryPolicy = policy

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)

    }

    private fun loadData(token: String) {
        val url = "https://collegehelperapi.herokuapp.com/api/user/auth/"

        val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, { response ->
            if (response.getBoolean("success")) {
                loadClassItems( token)
            }
        }, { error ->
            Log.e(TAG, "$error : ${error.message}")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["Authorization"] = token
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun loadClassItems(token: String) {
        val url = "https://collegehelperapi.herokuapp.com/api/user/class/"

        val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, { response ->
            if (response.getBoolean("success")) {
                val classes = response.getJSONArray("classes")
                Log.d(TAG, "Classes $classes")
                for (i in 0 until classes.length()) {
                    val classJsonObject = classes[0] as JSONObject
                    val className = classJsonObject.getString("className")
                    val subName = classJsonObject.getString("subName")
                    val classMongoId = classJsonObject.getString("_id")
                    val classItem = ClassItem(0, className, subName, classMongoId)
                    attendanceViewModel.insertClassItem(classItem)
                }
                loadStudent(token)
            }
        }, {
            Log.e(TAG, "$it : ${it.message}")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["Authorization"] = token
                return params
            }
        }
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(jsonObjectRequest)

    }

    private fun loadStudent(token: String) {
        attendanceViewModel.allClassItems.observe(this) { list ->
            for (item in list) {
                getClassStudent(item, token)
            }
        }
    }

    private fun getClassStudent(item: ClassItem, token: String) {
        val url = "https://collegehelperapi.herokuapp.com/api/user/student/${item.mongoId}"

        val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, { response ->
            if (response.getBoolean("success")) {
                val students = response.getJSONArray("students")
                Log.d(TAG, "Students $students")
                for (i in 0 until students.length()) {
                    val studentJsonObject = students[i] as JSONObject
                    val studentName = studentJsonObject.getString("studentName")
                    val roll = studentJsonObject.getString("roll").toInt()
                    val studentMongoId = studentJsonObject.getString("_id")
                    studentActivityViewModel.insertStudent(
                        Student(
                            0,
                            item.cid,
                            studentName,
                            roll,
                            studentMongoId
                        )
                    )
                }
                loadStatus(token)
            }
        }, {
            Log.e(TAG, "$it : ${it.message}")

        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["Authorization"] = token
                return params
            }
        }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObjectRequest)
    }

    private fun loadStatus(token: String) {
        studentActivityViewModel.allStudentItems.observe(this) { students ->
            val size = students.size
            for (i in 0 until size) {
                val student = students[i]
                attendanceViewModel.getStudentMongoId(student.cid).observe(this) { classMongoId ->
                    loadStudentStatus(student.studentMongoId, classMongoId, student, token)
                }
            }
        }

    }

    private fun loadStudentStatus(
        studentMongoId: String,
        classMongoId: String?,
        student: Student,
        token: String
    ) {
        val url = "https://collegehelperapi.herokuapp.com/api/user/status"

        val map = HashMap<String, String>()
        map["classId"] = "$classMongoId"
        map["studentId"] = studentMongoId
        val jsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url, JSONObject(map as Map<*, *>), { response ->
                if (response.getBoolean("success")) {
                    val statuses = response.getJSONArray("statuses")
                    Log.d(TAG, "Statuses $statuses")
                    val size = statuses.length()
                    for (i in 0 until size) {
                        val status = statuses[i] as JSONObject
                        val attendanceStatus = status.getString("status")
                        val dateKey = status.getString("date")
                        val statusMongoId = status.getString("_id")
                        studentActivityViewModel.insertStatus(
                            Status(
                                0,
                                student.sid,
                                student.cid,
                                attendanceStatus,
                                dateKey,
                                statusMongoId
                            )
                        )
                    }
                    startActivity(Intent(this, MainActivity::class.java))


                }
            }, {
                Log.e(TAG, "$it : ${it.message}")
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["Content-Type"] = "application/json"
                    params["Authorization"] = token
                    return params
                }
            }

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(jsonObjectRequest)
    }

    private val thread = object : Thread() {
        override fun run() {
            try {
                sleep(5000)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val collegePref = getSharedPreferences("user_college_helper", MODE_PRIVATE)
        if (collegePref.contains("token")) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}