package com.example.galendar

import retrofit2.http.Body
import retrofit2.Call
import retrofit2.http.POST

// 회원가입 인터페이스
interface SignupService {
    @POST("/auth/signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignUpResponse>
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

// 로그인 인터페이스
interface LoginService {
    @POST("/auth")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}

// 로그인 요청 데이터 클래스
data class LoginRequest(
    val email: String,
    val password: String
)

// 로그인 응답 데이터 클래스
data class LoginResponse(
    val status: Int,
    val message: String,
    val data: LoginData?
)

// 로그인 데이터 클래스
data class LoginData(
    val accessToken: String,
    val refreshToken: String
)

// 토큰 갱신 인터페이스
interface RefreshService {
    @POST("/auth/refresh")
    suspend fun refreshToken(@Body refreshRequest: RefreshRequest): RefreshResponse
}

// 토큰 갱신 요청 데이터 클래스
data class RefreshRequest(val refreshToken: String)

// 토큰 갱신 응답 데이터 클래스
data class RefreshResponse(
    val status: Int,
    val message: String,
    val data: RefreshData? // RefreshData로 수정
)

// 토큰 갱신 데이터 클래스
data class RefreshData(
    val token: String, // 새 액세스 토큰
    val refreshToken: String // 새 갱신 토큰
)

interface EmailService {
    @POST("/email/verify")
    fun verifyEmailCode(@Body verifyRequest: VerifyRequest): Call<VerifyResponse>

    @POST("/email/send")
    fun sendEmail(@Body sendEmailRequest: SendEmailRequest): Call<SendEmailResponse>
}

data class VerifyRequest(
    val email: String,
    val code: String // 인증번호를 String 타입으로 수정
)

data class VerifyResponse(
    val status: Int,
    val message: String,
    val data: Any?
)

data class SendEmailRequest(
    val email: String
)

data class SendEmailResponse(
    val status: Int,
    val message: String,
    val data: Any?
)
