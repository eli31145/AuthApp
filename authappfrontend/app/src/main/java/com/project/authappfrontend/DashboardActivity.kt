package com.project.authappfrontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var role: String
    private lateinit var name: String
    private lateinit var btnLogout: Button
    private lateinit var btnResetPassword: Button
    private lateinit var btnChangePassword: Button
    private lateinit var btnUpdateProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btnLogout = findViewById(R.id.btnLogout)
        btnResetPassword = findViewById(R.id.btnToResetPassword)
        btnChangePassword = findViewById(R.id.btnToChangePassword)
        btnUpdateProfile = findViewById(R.id.btnToUpdateProfile)

        role = intent.getStringExtra(KEY_ROLE).toString().uppercase()
        name = intent.getStringExtra(KEY_USER).toString()

        val welcomeText = "Welcome $name!"
        val tvWelcome:TextView = findViewById(R.id.tvSubtitle)
        tvWelcome.text = welcomeText

        if (role == "ADMIN") {
            btnResetPassword.visibility = View.VISIBLE
        } else {
            btnResetPassword.visibility = View.GONE
        }

        //todo handle logout button
        //btnLogout.setOnClickListener { logoutUser() }

        btnResetPassword.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, ResetActivity::class.java).putExtra(
                KEY_ROLE, role).putExtra(KEY_USER, name))
        }
        btnChangePassword.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, ChangePasswordActivity::class.java).putExtra(
                KEY_ROLE, role).putExtra(KEY_USER, name))
        }
        btnUpdateProfile.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, UpdateProfileActivity::class.java).putExtra(
                KEY_ROLE, role).putExtra(KEY_USER, name))
        }
    }

    private fun logoutUser(){
        //todo
    }

}