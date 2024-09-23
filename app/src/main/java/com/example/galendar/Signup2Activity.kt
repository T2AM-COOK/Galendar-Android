package com.example.galendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signup2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup2)

        // `main` 뷰의 윈도우 인셋을 처리합니다.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // `back` 버튼 클릭 리스너 설정
        val backButton: ImageView = findViewById(R.id.back)
        backButton.setOnClickListener {
            onBackPressed() // 뒤로 가기 동작 호출
        }

        val next2: Button = findViewById(R.id.Next2)
        next2.setOnClickListener {
            val intent = Intent(this, Signup3Activity::class.java)
            startActivity(intent)
        }
    }
}
