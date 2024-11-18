package com.example.galendar

import retrofit2.http.Body
import retrofit2.Call
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.GET

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


interface ContestService {
    @GET("/contest/list")
    suspend fun getContestList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("keyword") keyword: String = "",
        @Query("targets") targets: List<Int> = emptyList(),
        @Query("regions") regions: List<Int> = emptyList(),
        @Query("submitStartDate") submitStartDate: String = "",
        @Query("submitEndDate") submitEndDate: String = ""
    ): ContestResponse
}

data class ContestResponse(
    val status: Int,
    val message: String,
    val data: List<ContestData>
)

data class ContestData(
    val id: Int,
    val title: String,
    val content: String,
    val cost: String,
    val link: String,
    val imgLink: String,
    val submitStartDate: String,
    val submitEndDate: String,
    val contestStartDate: String?, // nullable로 유지
    val contestEndDate: String? // nullable로 유지
)

interface region{
    @GET("/region")
    suspend fun Region(): RegionResponse
}

data class RegionResponse(
    val status: Int,
    val message: String,
    val data: List<RegionData>
)

data class RegionData(
    val id: Int,
    val name: String
)

interface target{
    @GET("/target")
    suspend fun Target() : TargetResponse
}
data class TargetResponse(
    val status: Int,
    val message: String,
    val data: List<TargetData>
)
data class TargetData(
    val id: Int,
    val name: String
)
