package com.example.galendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Signup2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup2)

        // Retrofit 초기화
        val retrofit = Retrofit.Builder()
            .baseUrl("https://3.37.189.59:8080") // 서버 주소로 변경해주세요.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val backButton: ImageView = findViewById(R.id.back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val username = intent.getStringExtra("username") ?: ""

        val emailText: EditText = findViewById(R.id.email) // 이메일
        val sendNumberText: EditText = findViewById(R.id.sendNumber) // 인증번호
        val sendEmailBtn: ImageView = findViewById(R.id.sendBtn) // 이메일 전송 버튼
        val nextBtn: Button = findViewById(R.id.Next2) // 다음 버튼

        sendEmailBtn.setOnClickListener {
            val email = emailText.text.toString().trim()
            if (email.isNotEmpty()) {
                //나중에 만들어넣기
            } else {
                Toast.makeText(this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        nextBtn.setOnClickListener {
            val sendNumber = sendNumberText.text.toString().trim()
            if (sendNumber.isNotEmpty()) {
                // 인증번호가 맞으면 다음 페이지로 이동
                val intent = Intent(this, Signup3Activity::class.java).apply {
                    putExtra("username", username)
                    putExtra("email", emailText.text.toString().trim())
                    putExtra("sendNumber", sendNumber)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "인증번호를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

