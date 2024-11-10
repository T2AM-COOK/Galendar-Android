package com.example.galendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://3.37.189.59"

    // Retrofit 인스턴스를 lazy 방식으로 초기화
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 서비스 객체들을 lazy 방식으로 초기
    val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }
    val EmailService: EmailService by lazy {
        retrofit.create(EmailService::class.java)
    }

    val signupService: SignupService by lazy {
        retrofit.create(SignupService::class.java)
    }

    val refreshService: RefreshService by lazy {
        retrofit.create(RefreshService::class.java)
    }
}
