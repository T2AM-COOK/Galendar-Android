package com.example.galendar.feature

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.ProgressBar
import android.view.View
import com.example.galendar.R

class Signup1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup1)

        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        val backButton: ImageView = findViewById(R.id.back)
        backButton.setOnClickListener {
            onBackPressed() // 뒤로 가기 동작 호출
        }

        val username: EditText = findViewById(R.id.username)
        val nextBtn: Button = findViewById(R.id.Next1)

        nextBtn.setOnClickListener {
            val name = username.text.toString().trim()

            if (name.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                val intent = Intent(this, Signup2Activity::class.java).apply {
                    putExtra("username", name)
                }
                startActivity(intent)

                // 화면이 넘어간 후에는 progressBar가 자동으로 숨겨지도록 지연 설정
                progressBar.postDelayed({
                    progressBar.visibility = View.GONE
                }, 1000) // 1초 후에 progressBar가 숨겨짐
            } else {
                Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
