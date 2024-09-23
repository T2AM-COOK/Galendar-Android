package com.example.galendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Apply window insets to the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the TextView and set up an OnClickListener
        val signupTextButton: TextView = findViewById(R.id.SignupTextButton)
        signupTextButton.setOnClickListener {
            val intent = Intent(this, Signup1Activity::class.java)
            startActivity(intent)
        }
        val LoginButton : Button = findViewById(R.id.LoginButton)
        LoginButton.setOnClickListener {
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }
    }
}