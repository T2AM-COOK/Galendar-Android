package com.example.galendar.remote

import retrofit2.http.Body
import retrofit2.Call
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.GET

// 회원가입 인터페이스
interface RetrofitService {
    @POST("/auth/signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignUpResponse>
    @POST("/auth")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
    @POST("/auth/refresh")
    suspend fun refreshToken(@Body refreshRequest: RefreshRequest): RefreshResponse
    @POST("/email/verify")
    fun verifyEmailCode(@Body verifyRequest: VerifyRequest): Call<VerifyResponse>
    @POST("/email/send")
    fun sendEmail(@Body sendEmailRequest: SendEmailRequest): Call<SendEmailResponse>
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
    @GET("/region")
    suspend fun Region(): RegionResponse

    @GET("/target")
    suspend fun Target() : TargetResponse
}


