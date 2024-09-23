package com.example.galendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signup1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup1)

        val backButton: ImageView = findViewById(R.id.back)
        backButton.setOnClickListener {
            onBackPressed() // 뒤로 가기 동작 호출
        }

        val next1: Button = findViewById(R.id.Next1)
        next1.setOnClickListener {
            val intent = Intent(this, Signup2Activity::class.java)
            startActivity(intent)
        }
    }
}