package com.example.galendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope

class Signup3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup3)

        // 시스템 바 적용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val back: ImageView = findViewById(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }

        val next3: Button = findViewById(R.id.Next3)
        next3.setOnClickListener {
            val password: EditText = findViewById(R.id.password)
            val passwordCheck: EditText = findViewById(R.id.passwordCheck)
            val name = intent.getStringExtra("username") ?: ""
            val email = intent.getStringExtra("email") ?: ""

            // 입력한 비밀번호 문자열을 비교
            if (password.text.toString() == passwordCheck.text.toString()) {
                signup(name, email, password.text.toString())
            } else {
                Toast.makeText(this, "비밀번호가 동일하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signup(name: String, email: String, password: String) {
        val signupRequest = SignupRequest(name, email, password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.signupService.signup(signupRequest)
                withContext(Dispatchers.Main) {
                    if (response.status == 200) { // 성공적인 응답 처리
                        Toast.makeText(this@Signup3Activity, "회원가입 성공: ${response.message}", Toast.LENGTH_LONG).show()
                        // 회원가입 성공 후 처리
                        // 예: 다음 화면으로 이동
                        // startActivity(Intent(this@Signup3Activity, NextActivity::class.java))
                    } else {
                        Toast.makeText(this@Signup3Activity, "회원가입 실패: ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Signup3Activity, "오류 발생", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}




