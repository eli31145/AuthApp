package com.project.authappfrontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.project.authappfrontend.enums.Role
import com.project.authappfrontend.models.ChangePasswordRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etOldPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var role: Role

    private lateinit var intentExtrasRole: String
    private lateinit var intentExtrasName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        intentExtrasRole = intent.getStringExtra(KEY_ROLE).toString().uppercase()
        intentExtrasName = intent.getStringExtra(KEY_USER).toString()

        etName = findViewById(R.id.etName)
        etOldPassword = findViewById(R.id.etOldPassword)
        etNewPassword = findViewById(R.id.etNewPassword)

        findViewById<Button>(R.id.btnToChangePassword).setOnClickListener { changePassword() }
    }

    private fun changePassword() {
        val name = etName.text.toString().trim()
        val oldPassword = etOldPassword.text.toString().trim()
        val newPassword = etNewPassword.text.toString().trim()

        if (name.isEmpty() || name.isBlank()){
            etName.error = "Name is required"
            etName.requestFocus()
            return
        } else if (oldPassword.isEmpty() || oldPassword.isBlank()){
            etOldPassword.error = "Current Password is required"
            etOldPassword.requestFocus()
            return
        } else if (newPassword.isEmpty() || newPassword.isBlank()){
            etNewPassword.error = "New Password is required"
            etNewPassword.requestFocus()
            return
        }

        if (name != intentExtrasName) {
            etName.error = "Name provided not the same as before"
            etName.requestFocus()
            return
        }

        if (oldPassword == newPassword) {
            etNewPassword.error = "New Password cannot be the same as old password"
            etNewPassword.requestFocus()
            return
        }

        val call: Call<ResponseBody> = RetrofitClient.getInstance().api.changePassword(ChangePasswordRequest(name, oldPassword, newPassword))
        call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var s = ""
                try {
                    s = response.body()!!.string()
                } catch (ioE: IOException) {
                    ioE.printStackTrace()
                }

                if (s.equals("Password changed successfully and saved")) {
                    Toast.makeText(this@ChangePasswordActivity, "Password changed successfully", Toast.LENGTH_SHORT).show()
                    //currently redirecting to DashboardActivity. Future consider automatically logging out and requesting user to log in again with new password
                    //Also hardcoding check of Role via passed intent, future consider making more dynamic if admin list expands)
                    if (intentExtrasRole == "ADMIN") {
                        role = Role.ADMIN
                    } else {
                        role = Role.USER
                    }
                    startActivity(Intent(this@ChangePasswordActivity, DashboardActivity::class.java).putExtra(
                        KEY_ROLE, role).putExtra(KEY_USER, name))
                } else {
                    Toast.makeText(this@ChangePasswordActivity, "$s", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ChangePasswordActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        } )
    }
}