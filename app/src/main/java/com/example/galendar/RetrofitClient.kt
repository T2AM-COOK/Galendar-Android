package com.example.galendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://3.37.189.59:8080"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val loginRetrofit: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

    // api는 retrofit 인스턴스 외부에서 초기화되어야 함
    val api: SignupService by lazy {
        retrofit.create(SignupService::class.java)
    }
    val signupService: SignupService = retrofit.create(SignupService::class.java)
}


