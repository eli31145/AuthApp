package com.project.authappfrontend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.project.authappfrontend.models.AuthRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

const val KEY_ROLE: String = "KEY_ROLE"
const val KEY_USER: String = "KEY_USER"
class LoginActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etName = findViewById(R.id.etName)
        etPassword = findViewById(R.id.etPassword)

        findViewById<Button>(R.id.btnLogin).setOnClickListener { loginUser() }

        findViewById<TextView>(R.id.tvRegisterLink).setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(){
        val name = etName.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (name.isEmpty() || name.isBlank()){
            etName.error = "Name is required"
            etName.requestFocus()
            return
        } else if (password.isEmpty() || name.isBlank()){
            etPassword.error = "Password is required"
            etPassword.requestFocus()
            return
        }

        val call: Call<ResponseBody> = RetrofitClient.getInstance().api.authenticateUser(AuthRequest(name, password))
        call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var s = ""
                try {
                    s = response.body()!!.string()
                } catch (ioE:IOException){
                    ioE.printStackTrace()
                }
                if (s.equals("ADMIN") || s.equals("USER")) {
                    Toast.makeText(this@LoginActivity, "User Logged in!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java).putExtra(
                        KEY_ROLE, s).putExtra(KEY_USER, name))
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid userid or password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
}