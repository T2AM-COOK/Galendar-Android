package com.example.galendar

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ProgressBar
import android.view.View

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var loginButton: Button
    private lateinit var authManager: AuthManager
    private lateinit var progressBar: ProgressBar // 프로그레스 바 변수 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // UI 요소 초기화
        emailEdit = findViewById(R.id.email)
        passwordEdit = findViewById(R.id.password)
        loginButton = findViewById(R.id.LoginButton)
        progressBar = findViewById(R.id.progressBar) // 프로그레스 바 초기화

        // AuthManager 초기화
        authManager = AuthManager(this)

        // 자동 로그인 체크
        checkAutoLogin()

        // 로그인 버튼 클릭 리스너 설정
        loginButton.setOnClickListener {
            val email = emailEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()

            // 이메일과 비밀번호 입력 확인
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 프로그레스 바 시작
            progressBar.visibility = View.VISIBLE

            // AuthManager의 로그인 메서드 호출
            authManager.login(email, password) { success ->
                // 로그인 완료 후 프로그레스 바 숨김
                progressBar.visibility = View.GONE

                if (success) {
                    // 로그인 성공 시 MainActivity로 이동
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent) // MainActivity로 이동
                    finish() // LoginActivity 종료
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
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
        // 프로그레스 바 시작
        progressBar.visibility = View.VISIBLE
        val accessToken = authManager.getAccessToken() // AuthManager를 통해 액세스 토큰 가져오기
        if (!accessToken.isNullOrEmpty()) {
            // 액세스 토큰이 유효하면 MainActivity로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // LoginActivity 종료

            // 화면이 넘어간 후에는 progressBar가 자동으로 숨겨지도록 지연 설정
            progressBar.postDelayed({
                progressBar.visibility = View.GONE
            }, 1000) // 1초 후에 progressBar가 숨겨짐
        }

        // 프로그레스 바 숨김 (자동 로그인 후)
        progressBar.visibility = View.GONE
    }
}

