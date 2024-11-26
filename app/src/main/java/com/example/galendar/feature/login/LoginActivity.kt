package com.example.galendar.feature.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.util.Log
import androidx.activity.viewModels
import com.example.galendar.R
import com.example.galendar.feature.main.MainActivity
import com.example.galendar.feature.Signup1Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    val viewModel: LoginViewModel by viewModels()

    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEdit = findViewById(R.id.email)
        passwordEdit = findViewById(R.id.password)
        loginButton = findViewById(R.id.LoginButton)
        progressBar = findViewById(R.id.progressBar)


        checkAutoLogin()

        loginButton.setOnClickListener {
            val email = emailEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            viewModel.login(email, password) { success ->
                progressBar.visibility = View.GONE

                if (success) {


                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val signupTextButton: TextView = findViewById(R.id.SignupTextButton)
        signupTextButton.setOnClickListener {
            val intent = Intent(this, Signup1Activity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun checkAutoLogin() {
        progressBar.visibility = View.VISIBLE
        val accessToken = viewModel.getAccessToken()

        if (!accessToken.isNullOrEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

            progressBar.postDelayed({
                progressBar.visibility = View.GONE
            }, 1000)
        } else {
            // 리프레시 토큰을 갱신하려고 시도
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.refreshAccessToken()

                // 갱신된 토큰을 바로 다시 가져와 확인
                val newAccessToken = viewModel.getAccessToken()
                Log.d("LoginActivity", "갱신된 액세스 토큰: $newAccessToken")

                if (!newAccessToken.isNullOrEmpty()) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "자동 로그인 실패: 다시 로그인해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }
                progressBar.visibility = View.GONE
            }
        }
    }
}