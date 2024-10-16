package com.example.galendar

import retrofit2.http.Body
import retrofit2.http.POST

// 회원가입 인터페이스
interface SignupService {
    @POST("/auth/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): SignUpResponse
}

// 회원가입 요청 데이터 클래스
data class SignupRequest(
    val name: String,
    val email: String,
    val password: String
)

// 회원가입 응답 데이터 클래스
data class SignUpResponse(
    val status: Int,
    val message: String,
    val data: Any? // nullable로 변경
)


interface  LoginService{
    @POST("/auth")
    suspend fun login(@Body loginRequest: loginRequest): loginResponse
}
data class loginRequest(
    val email : String,
    val password: String
)
data class loginResponse(
    val status: Int,
    val message: String,
    val data: Any?
)
