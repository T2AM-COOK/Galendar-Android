package com.example.galendar.feature.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.galendar.R
import com.example.galendar.feature.main.MainActivity
import com.example.galendar.feature.Signup1Activity
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    val viewModel: LoginViewModel by viewModels()

    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

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
            fetchFcmToken { token ->
                viewModel.login(email, password, token) { success ->
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

        requestNotificationPermissionIfNeeded();


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

    private fun fetchFcmToken(onTokenReceived: (String) -> Unit) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                val token = if (task.isSuccessful) task.result else ""
                onTokenReceived(token)
            }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST_CODE)
            } else {
                // 권한 O
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 승인된 경우
            } else {
                // 권한이 거부된 경우
            }
        }
    }

}
