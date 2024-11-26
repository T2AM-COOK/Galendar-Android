package com.example.galendar.remote

import okhttp3.Interceptor
import okhttp3.Response

// 액세스 토큰을 헤더에 자동으로 추가하는 인터셉터
object TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // TokenManager에서 저장된 액세스 토큰을 가져옵니다.
        val token = TokenManager.getToken()

        // 요청을 생성할 때, 토큰이 있을 경우 Authorization 헤더에 추가합니다.
        val newRequest = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")  // Bearer 방식으로 토큰을 추가
                .build()
        } else {
            chain.request()  // 토큰이 없으면 그대로 요청
        }

        return chain.proceed(newRequest)  // 새로 생성된 요청을 서버에 전달
    }
}
