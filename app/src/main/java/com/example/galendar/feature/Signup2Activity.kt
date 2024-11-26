package com.example.galendar.feature

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.galendar.R
import com.example.galendar.remote.RetrofitBuilder
import com.example.galendar.remote.SendEmailRequest
import com.example.galendar.remote.SendEmailResponse
import com.example.galendar.remote.VerifyRequest
import com.example.galendar.remote.VerifyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Signup2Activity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup2)

        val backButton: ImageView = findViewById(R.id.back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val username = intent.getStringExtra("username") ?: ""

        val emailText: EditText = findViewById(R.id.email) // 이메일
        val sendNumberText: EditText = findViewById(R.id.sendNumber) // 인증번호
        val sendEmailBtn: ImageView = findViewById(R.id.sendBtn) // 이메일 전송 버튼
        val nextBtn: Button = findViewById(R.id.Next2) // 다음 버튼
        progressBar = findViewById(R.id.progressBar)

        sendEmailBtn.setOnClickListener {
            val email = emailText.text.toString().trim()

            if (email.isNotEmpty()) {
                sendEmail(email)
            } else {
                Toast.makeText(this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        nextBtn.setOnClickListener {
            val sendNumber = sendNumberText.text.toString().trim()
            if (sendNumber.isNotEmpty()) {
                verifyEmailCode(emailText.text.toString().trim(), sendNumber)
            } else {
                Toast.makeText(this, "인증번호를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 이메일로 인증번호 보내기
    private fun sendEmail(email: String) {
        progressBar.visibility = View.VISIBLE

        val sendEmailRequest = SendEmailRequest(email)
        RetrofitBuilder.apiService.sendEmail(sendEmailRequest).enqueue(object : Callback<SendEmailResponse> {
            override fun onResponse(call: Call<SendEmailResponse>, response: Response<SendEmailResponse>) {
                progressBar.visibility = View.GONE
                val sendEmailResponse = response.body()

                if (sendEmailResponse != null && sendEmailResponse.status == 200) {
                    Toast.makeText(this@Signup2Activity, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Signup2Activity, "이메일 전송 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SendEmailResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@Signup2Activity, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }


    // 인증번호 확인
    private fun verifyEmailCode(email: String, code: String) {
        progressBar.visibility = View.VISIBLE

        val verifyRequest = VerifyRequest(email, code)
        RetrofitBuilder.apiService.verifyEmailCode(verifyRequest).enqueue(object : Callback<VerifyResponse> {
            override fun onResponse(call: Call<VerifyResponse>, response: Response<VerifyResponse>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val verifyResponse = response.body()

                    if (verifyResponse != null && verifyResponse.status == 200) {
                        // 인증 성공 -> 다음 화면으로 이동
                        val intent = Intent(this@Signup2Activity, Signup3Activity::class.java)
                        intent.putExtra("username", intent.getStringExtra("username"))
                        intent.putExtra("email", email)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@Signup2Activity, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Signup2Activity, "인증 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<VerifyResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@Signup2Activity, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

