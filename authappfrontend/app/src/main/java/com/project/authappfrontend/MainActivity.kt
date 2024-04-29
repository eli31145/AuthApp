package com.project.authappfrontend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnToRegister).setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        }

        findViewById<Button>(R.id.btnToLogin).setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }
}