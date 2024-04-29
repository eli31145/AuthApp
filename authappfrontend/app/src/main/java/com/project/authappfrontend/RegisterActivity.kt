package com.project.authappfrontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.project.authappfrontend.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    private lateinit var etRName: EditText
    private lateinit var etRPhone: EditText
    private lateinit var etRAddress: EditText
    private lateinit var etREmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etRName = findViewById(R.id.etRName)
        etRPhone = findViewById(R.id.etRPhone)
        etRAddress = findViewById(R.id.etRAddress)
        etREmail = findViewById(R.id.etREmail)

        findViewById<Button>(R.id.btnRegister).setOnClickListener { registerUser() }

        findViewById<TextView>(R.id.tvLoginLink).setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerUser(){
        val newName = etRName.text.toString().trim()
        val newPhone = etRPhone.text.toString().trim()
        val newAddress = etRAddress.text.toString().trim()
        val newEmail = etREmail.text.toString().trim()

        if(newName.isEmpty() || newName.isBlank()){
            etRName.error = "Username is required"
            etRName.requestFocus()
            return
        } else if (newPhone.isEmpty() || newPhone.isBlank()){
            etRPhone.error = "Phone number is required"
            etRPhone.requestFocus()
            return
        } else if (newAddress.isEmpty() || newAddress.isBlank()) {
            etRAddress.error = "Address is required"
            etRAddress.requestFocus()
            return
        } else if (newEmail.isEmpty() || newEmail.isBlank()) {
            etREmail.error = "Email is required"
            etREmail.requestFocus()
            return
        }

        val call: Call<ResponseBody> = RetrofitClient.getInstance().api.onboardUser(User(newName, newEmail, newPhone, newAddress, null, null, null, null, null, null))
        call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var s = ""
                try {
                    s = response.body()!!.string()
                } catch (ioE: IOException) {
                    ioE.printStackTrace()
                }

                if (s.equals("User onboarded successfully. Password sent to terminal.")){
                    Toast.makeText(this@RegisterActivity, "Successfully registered. Please login", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                } else {
                    Toast.makeText(this@RegisterActivity, "Received $s", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

}