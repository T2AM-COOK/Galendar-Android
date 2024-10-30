package com.example.galendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import android.view.View

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
        val progressBar : ProgressBar = findViewById(R.id.progressBar)

        RetrofitClient.signupService.signup(signupRequest)
            .enqueue(object : retrofit2.Callback<SignUpResponse> {
                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: retrofit2.Response<SignUpResponse>
                ) {
                    if (response.isSuccessful) { // 2xx 상태 코드일 경우
                        progressBar.visibility = View.VISIBLE
                        val responseBody = response.body()
                        if (responseBody != null && responseBody.status == 200) {
                            Toast.makeText(
                                this@Signup3Activity,
                                "회원가입 성공: ${responseBody.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this@Signup3Activity, LoginActivity::class.java))
                            // 화면이 넘어간 후에는 progressBar가 자동으로 숨겨지도록 지연 설정
                            progressBar.postDelayed({
                                progressBar.visibility = View.GONE
                            }, 1000) // 1초 후에 progressBar가 숨겨짐
                        } else {
                            Toast.makeText(
                                this@Signup3Activity,
                                "회원가입 실패: ${responseBody?.message ?: "알 수 없는 오류"}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@Signup3Activity, "서버 오류: $errorBody", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    Toast.makeText(this@Signup3Activity, "네트워크 오류: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }
}




