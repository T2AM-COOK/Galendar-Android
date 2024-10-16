package com.example.galendar

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val emaiTextl = intent.getStringExtra("email") ?: ""
        val passwordText = intent.getStringExtra("password")?:""
        val loginBtn : Button = findViewById(R.id.LoginButton)
        val emailEdit : EditText = findViewById<EditText?>(R.id.email)
        val passwordEdit : EditText = findViewById<EditText?>(R.id.password)

        loginBtn.setOnClickListener(){
            val email = emailEdit.text.toString() //로그인에서 입력한 이메일
            val password= passwordEdit.text.toString() //로그인에서 입력한 비번

            var loginRetrofit = retrofit.create(LoginService::class.java)
            val loginRequest = loginRequest(email, password)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = loginRetrofit.login(loginRequest)
                    if (response.status == 200) {
                        runOnUiThread {
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }




        // Apply window insets to the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 회원가입 텍스트 클릭 리스너
        val signupTextButton: TextView = findViewById(R.id.SignupTextButton)
        signupTextButton.setOnClickListener {
            val intent = Intent(this, Signup1Activity::class.java)
            startActivity(intent)
        }
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://3.37.189.59:8080") // 서버 주소로 변경해주세요.
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var loginRetrofit = retrofit.create(LoginService::class.java)






    private fun loadEncryptedPassword(): Pair<ByteArray, ByteArray> {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val encryptedPasswordString = sharedPreferences.getString("encryptedPassword", null)
        val ivString = sharedPreferences.getString("iv", null)

        val encryptedPassword = encryptedPasswordString?.let { Base64.decode(it, Base64.DEFAULT) } ?: ByteArray(0)
        val iv = ivString?.let { Base64.decode(it, Base64.DEFAULT) } ?: ByteArray(0)

        return Pair(encryptedPassword, iv)
    }

    private fun saveEncryptedPassword(encryptedPassword: ByteArray, iv: ByteArray) {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("encryptedPassword", Base64.encodeToString(encryptedPassword, Base64.DEFAULT))
        editor.putString("iv", Base64.encodeToString(iv, Base64.DEFAULT))

        editor.apply()
    }
}



