package com.project.authappfrontend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ResetActivity : AppCompatActivity() {
    private lateinit var intentExtrasRole: String
    private lateinit var intentExtrasName: String

    private lateinit var etResetName: EditText
    private lateinit var etResetEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_actvity)

        intentExtrasRole = intent.getStringExtra(KEY_ROLE).toString().uppercase()
        intentExtrasName = intent.getStringExtra(KEY_USER).toString()

        etResetName = findViewById(R.id.etNameToReset)
        etResetEmail = findViewById(R.id.etEmailToReset)

        findViewById<Button>(R.id.btnUpdateProfile).setOnClickListener { resetPassword() }
    }


    private fun resetPassword() {
        val name = etResetName.text.toString().trim()
        val email = etResetEmail.text.toString().trim()

        if (name.isEmpty() || name.isBlank()){
            etResetName.error = "Name is required"
            etResetName.requestFocus()
            return
        } else if (email.isEmpty() || email.isBlank()){
            etResetEmail.error = "Email is required"
            etResetEmail.requestFocus()
            return
        }

        val call: Call<ResponseBody> = RetrofitClient.getInstance().api.resetPassword(name, email)
        call.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var s = ""
                try {
                    s = response.body()!!.string()
                } catch (ioE: IOException) {
                    ioE.printStackTrace()
                }

                if (s.equals("Password reset successfully. Password sent to terminal.")) {
                    Toast.makeText(this@ResetActivity, "Password reset successfully. Password sent to terminal.", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(this@ResetActivity, DashboardActivity::class.java).putExtra(
                        KEY_ROLE, intentExtrasRole).putExtra(KEY_USER, intentExtrasName))
                } else {
                    Toast.makeText(this@ResetActivity, "$s", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ResetActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        } )
    }
}