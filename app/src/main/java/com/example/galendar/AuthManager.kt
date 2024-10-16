package com.example.galendar

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AuthManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("GalendarPrefs", Context.MODE_PRIVATE)

    private val loginService = RetrofitClient.loginService
    private val refreshService = RetrofitClient.refreshService

    // 로그인 함수
    fun login(email: String, password: String, callback: (Boolean) -> Unit) {
        val loginRequest = LoginRequest(email, password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = loginService.login(loginRequest)
                withContext(Dispatchers.Main) {
                    if (response.status == 200) {
                        response.data?.let {
                            val accessToken = it.accessToken
                            val refreshToken = it.refreshToken

                            // 토큰 저장
                            sharedPreferences.edit().putString("accessToken", accessToken).apply()
                            sharedPreferences.edit().putString("refreshToken", refreshToken).apply()

                            Log.d("AuthManager", "로그인 성공: 토큰 저장 완료")
                            callback(true) // 로그인 성공
                        } ?: run {
                            Log.e("AuthManager", "로그인 데이터가 null입니다.")
                            callback(false) // 로그인 실패
                        }
                    } else {
                        Log.e("AuthManager", "로그인 실패: ${response.message}")
                        callback(false) // 로그인 실패
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Log.e("AuthManager", "로그인 에러: ${e.message()}")
                    callback(false) // 로그인 실패
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("AuthManager", "알 수 없는 에러: ${e.message}")
                    callback(false) // 로그인 실패
                }
            }
        }
    }

    // 토큰 갱신 함수
    fun refreshAccessToken() {
        val refreshToken = sharedPreferences.getString("refreshToken", null)

        if (refreshToken != null) {
            val refreshRequest = RefreshRequest(refreshToken)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = refreshService.refreshToken(refreshRequest)
                    withContext(Dispatchers.Main) {
                        if (response.status == 200) {
                            response.data?.let {
                                val newAccessToken = it.token
                                val newRefreshToken = it.refreshToken

                                // 새 토큰 저장
                                sharedPreferences.edit().putString("accessToken", newAccessToken).apply()
                                sharedPreferences.edit().putString("refreshToken", newRefreshToken).apply()

                                Log.d("AuthManager", "토큰 갱신 성공: 새 토큰 저장")
                                showToast("토큰 갱신 성공")
                            } ?: run {
                                Log.e("AuthManager", "갱신된 데이터가 null입니다.")
                                showToast("토큰 갱신 실패: 데이터가 유효하지 않습니다.")
                            }
                        } else {
                            Log.e("AuthManager", "토큰 갱신 실패: ${response.message}")
                            showToast("토큰 갱신 실패: ${response.message}")
                        }
                    }
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        Log.e("AuthManager", "토큰 갱신 에러: ${e.message()}")
                        showToast("토큰 갱신 에러")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("AuthManager", "알 수 없는 에러: ${e.message}")
                        showToast("알 수 없는 에러 발생")
                    }
                }
            }
        } else {
            Log.e("AuthManager", "저장된 refreshToken이 없습니다.")
            showToast("저장된 refreshToken이 없습니다.")
        }
    }

    // Access Token 가져오는 메서드
    fun getAccessToken(): String? {
        return sharedPreferences.getString("accessToken", null)
    }

    // 토스트 메시지를 표시하는 함수
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
