package com.example.galendar.feature.login

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.galendar.data.ACCESSTOKEN
import com.example.galendar.data.PREFKEY
import com.example.galendar.data.REFRESHTOKEN
import com.example.galendar.remote.LoginRequest
import com.example.galendar.remote.RefreshRequest
import com.example.galendar.remote.RetrofitBuilder
import com.example.galendar.remote.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import com.example.galendar.remote.TokenManager

class LoginViewModel(application: Application) : AndroidViewModel(application = application) {
    val sharedPref = application.getSharedPreferences(PREFKEY, Context.MODE_PRIVATE)

    fun login(email: String, password: String, success: (Boolean) -> Unit) {
        val loginRequest = LoginRequest(email, password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitBuilder.apiService.login(loginRequest)
                withContext(Dispatchers.Main) {
                    if (response.status == 200) {
                        response.data?.let {
                            val accessToken = it.accessToken
                            val refreshToken = it.refreshToken

                            TokenManager.setToken(accessToken)


                            sharedPref.edit().putString("accessToken", accessToken).apply()
                            sharedPref.edit().putString("refreshToken", refreshToken).apply()
                            success(true)
                        } ?: run {
                            Log.e("login", "token null")
                            success(false)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("login", "login failed")
            }
        }
    }


    fun refreshAccessToken() {
        val refreshToken = sharedPref.getString(REFRESHTOKEN, null)
        if (refreshToken == null) {
            Log.e("AuthManager", "저장된 refreshToken이 없습니다.")
        }

        if (refreshToken != null) {
            val refreshRequest = RefreshRequest(refreshToken)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitBuilder.apiService.refreshToken(refreshRequest)
                    withContext(Dispatchers.Main) {
                        if (response.status == 200) {
                            response.data?.let {
                                val newAccessToken = it.token
                                val newRefreshToken = it.refreshToken

                                // 새 토큰 저장
                                sharedPref.edit().putString("accessToken", newAccessToken)
                                    .apply()
                                sharedPref.edit().putString("refreshToken", newRefreshToken)
                                    .apply()

                                Log.d("AuthManager", "토큰 갱신 성공: 새 토큰 저장")
                            } ?: run {
                                Log.e("AuthManager", "갱신된 데이터가 null입니다.")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("refresh", "refresh failed")
                }
            }
        } else {
            Log.e("refresh", "저장된 refreshToken이 없습니다.")
        }
    }

    fun getAccessToken(): String? {
        return sharedPref.getString(ACCESSTOKEN, null)
    }
}