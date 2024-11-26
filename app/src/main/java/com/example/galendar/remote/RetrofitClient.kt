package com.example.galendar.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    companion object {
        private val BASE_URL = "http://3.37.189.59"

        private fun getRetrofit(): Retrofit {
            // OkHttpClient 빌더 설정
            val client = OkHttpClient.Builder()
                // 로그를 위한 HttpLoggingInterceptor
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                // TokenInterceptor 추가 (액세스 토큰을 헤더에 자동으로 추가)
                .addInterceptor(TokenInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)  // OkHttpClient 설정
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val apiService: RetrofitService = getRetrofit().create(RetrofitService::class.java)
    }
}
