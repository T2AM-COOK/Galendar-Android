package com.example.galendar.feature.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.fragment.app.Fragment
import com.example.galendar.R
import kotlinx.coroutines.launch
import com.example.galendar.feature.login.LoginActivity
import com.example.galendar.remote.RetrofitBuilder
import com.example.galendar.remote.RetrofitService
import com.example.galendar.remote.MeList
import com.example.galendar.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.io.IOException

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        // WindowInsets 설정
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.profile_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 서버 데이터 가져오기
        fetchProfileData()
    }
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        showError("서버와 통신 중 오류가 발생했습니다. ${exception.localizedMessage}")
    }

    private fun fetchProfileData() {
        lifecycleScope.launch(exceptionHandler) {
            try {
                val response = RetrofitBuilder.apiService.getme()
                if (response.status == 200) {
                    val userData = response.data
                    if (userData != null) {
                        updateUI(userData)
                    } else {
                        showError("사용자 정보를 찾을 수 없습니다.")
                    }
                } else {
                    showError(response.message)
                }
            } catch (e: Exception) {
                when (e) {
                    is IOException -> showError("네트워크 오류가 발생했습니다.")
                    is HttpException -> showError("서버와 통신 중 오류가 발생했습니다. (${e.code()})")
                    is JsonParseException -> showError("응답 데이터 파싱 오류가 발생했습니다.")
                    else -> showError("알 수 없는 오류가 발생했습니다.")
                }
            }
        }
    }


            private fun updateUI(userData: MeList) {
        binding.greetingText.text = "${userData.name}님, 안녕하세요!"
        binding.nameValue.text = userData.name
        binding.emailValue.text = userData.email
    }

    private fun showError(message: String) {
        // 오류 메시지 표시 (예: 토스트로 알림)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
