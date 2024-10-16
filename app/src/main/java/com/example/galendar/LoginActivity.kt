package com.example.galendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var loginButton: Button
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // UI 요소 초기화
        emailEdit = findViewById(R.id.email)
        passwordEdit = findViewById(R.id.password)
        loginButton = findViewById(R.id.LoginButton)

        // AuthManager 초기화
        authManager = AuthManager(this)

        // 자동 로그인 체크
        checkAutoLogin()

        // 로그인 버튼 클릭 리스너 설정
        loginButton.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()

            // AuthManager의 로그인 메서드 호출
            authManager.login(email, password) { success ->
                if (success) {
                    // 로그인 성공 시 홈 화면으로 이동
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티 종료
                    Toast.makeText(this,"로그인 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 회원가입 텍스트 클릭 리스너
        val signupTextButton: TextView = findViewById(R.id.SignupTextButton)
        signupTextButton.setOnClickListener {
            val intent = Intent(this, Signup1Activity::class.java)
            startActivity(intent)
        }

        // Apply window insets to the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkAutoLogin() {
        val accessToken = authManager.getAccessToken() // AuthManager를 통해 액세스 토큰 가져오기
        if (!accessToken.isNullOrEmpty()) {
            // 액세스 토큰이 유효하면 홈 화면으로 이동
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }
    }
}
