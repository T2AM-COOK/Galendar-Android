package com.example.galendar

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    private const val BASE_URL = "http://3.37.189.59/"

    // OkHttpClient를 설정하여 Bearer 토큰을 Authorization 헤더에 추가
    private val client = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val token = "eyJKV1QiOiJBQ0NFU1MiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJza3NkbGRid2xzMDUyN0BnbWFpbC5jb20iLCJpYXQiOjE3MzE5Mzg0OTEsImV4cCI6MTczMTkzOTM5MX0.3g0WcWP8FW-Fs86jeQFpBIDv4jjG9RZLO9bmQsV7KdY" // 여기에 실제 access token을 넣어야 합니다.
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")  // Bearer 토큰 추가
                .build()
            chain.proceed(newRequest)
        }
    }.build()

    // Retrofit 인스턴스를 생성할 때 OkHttpClient 설정 적용
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // OkHttpClient 적용
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 서비스 객체들을 lazy 방식으로 초기화
    val contestService: ContestService by lazy {
        retrofit.create(ContestService::class.java)
    }

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
