package com.example.galendar.remote


object TokenManager {
    // 액세스 토큰을 저장하는 변수
    private var authToken: String? = null

    // 액세스 토큰 저장하는 함수
    fun setToken(token: String) {
        authToken = token
    }

    // 저장된 액세스 토큰 반환하는 함수
    fun getToken(): String? {
        return authToken
    }

    // 액세스 토큰을 삭제하는 함수 (로그아웃 시 사용)
    fun clearToken() {
        authToken = null
    }
}
