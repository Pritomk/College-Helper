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

        }
    }

}