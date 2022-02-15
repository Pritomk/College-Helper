package com.example.collegehelper.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.ServerError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.collegehelper.MainActivity
import com.example.collegehelper.R
import com.example.collegehelper.SharedPreferenceClass
import com.example.collegehelper.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var nameText: EditText
    private lateinit var emailText: EditText
    private lateinit var passText: EditText
    private lateinit var loginBtn: TextView
    private lateinit var regBtn: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferenceClass: SharedPreferenceClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferenceClass = SharedPreferenceClass(this)

        nameText = binding.usernameET
        emailText = binding.emailET
        passText = binding.passwordET
        loginBtn = binding.loginBtn
        regBtn = binding.registerBtn
        progressBar = binding.progressBar

        firebaseAuth = Firebase.auth

        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        regBtn.setOnClickListener {
            regFunc()
        }
    }

    private fun regFunc() {
        progressBar.visibility = View.VISIBLE
        val username = nameText.text.toString()
        val email = emailText.text.toString()
        val password = passText.text.toString()

        val url = "https://collegehelperapi.herokuapp.com/api/user/auth/register"

        val map = HashMap<String, String>()
        map["username"] = username
        map["email"] = email
        map["password"] = password

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, JSONObject(map as Map<*, *>), { response ->
            try {
                if (response.getBoolean("success")) {
                    val token = response.getString("token")
                    sharedPreferenceClass.setValueString("token", token)
                    startActivity(Intent(this, MainActivity::class.java))
                    progressBar.visibility = View.GONE
                }
            } catch (e: JSONException) {
                progressBar.visibility = View.GONE
                e.printStackTrace()
            }
        },{ error ->
            val response = error.networkResponse
            if (error != null && error is ServerError) {
                try {
                    val res = String(response.data)
                    val obj = JSONObject(res)
                    Toast.makeText(this, "Error is ${obj.getString("msg")}", Toast.LENGTH_SHORT).show()
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

        val policy = DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        jsonObjectRequest.retryPolicy = policy

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }
}