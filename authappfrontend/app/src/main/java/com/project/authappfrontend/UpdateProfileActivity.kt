package com.project.authappfrontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.project.authappfrontend.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var intentExtrasRole: String
    private lateinit var intentExtrasName: String

    private lateinit var etUPhone: EditText
    private lateinit var etUAddress: EditText
    private lateinit var etUEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        intentExtrasRole = intent.getStringExtra(KEY_ROLE).toString().uppercase()
        intentExtrasName = intent.getStringExtra(KEY_USER).toString()

        etUPhone = findViewById(R.id.etUPhone)
        etUAddress = findViewById(R.id.etUAddress)
        etUEmail = findViewById(R.id.etUEmail)

        findViewById<Button>(R.id.btnUpdateProfile).setOnClickListener { updateProfile() }
    }

    private fun updateProfile() {
        val newPhone = etUPhone.text.toString().trim()
        val newAddress = etUAddress.text.toString().trim()
        val newEmail = etUEmail.text.toString().trim()

        if (newPhone.isEmpty() || newPhone.isBlank()){
            etUPhone.error = "Phone number is required"
            etUPhone.requestFocus()
            return
        } else if (newAddress.isEmpty() || newAddress.isBlank()){
            etUAddress.error = "Address is required"
            etUAddress.requestFocus()
            return
        } else if (newEmail.isEmpty() || newEmail.isBlank()){
            etUEmail.error = "Email is required"
            etUEmail.requestFocus()
            return
        }

        val call: Call<ResponseBody> = RetrofitClient.getInstance().api.updateProfile(User(intentExtrasName, newEmail, newPhone, newAddress, null, null, null, null, null, intentExtrasRole))
        call.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var s = ""
                try {
                    s = response.body()!!.string()
                } catch (ioE: IOException) {
                    ioE.printStackTrace()
                }

                if (s.equals("Profile updated Successfully!")){
                    Toast.makeText(this@UpdateProfileActivity, "Profile successfully updated", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@UpdateProfileActivity, DashboardActivity::class.java).putExtra(
                        KEY_ROLE, intentExtrasRole).putExtra(KEY_USER, intentExtrasName))
                } else {
                    Toast.makeText(this@UpdateProfileActivity, "Received $s", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@UpdateProfileActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}