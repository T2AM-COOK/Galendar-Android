package com.example.galendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Signup1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup1)

        val backButton: ImageView = findViewById(R.id.back)
        backButton.setOnClickListener {
            onBackPressed() // 뒤로 가기 동작 호출
        }

        val username: EditText = findViewById(R.id.username)
        val nextBtn: Button = findViewById(R.id.Next1)

        nextBtn.setOnClickListener {
            val name = username.text.toString().trim()

            if (name.isNotEmpty()) {
                val intent = Intent(this, Signup2Activity::class.java).apply {
                    putExtra("username", name)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
