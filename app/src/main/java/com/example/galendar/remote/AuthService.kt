package com.example.galendar.remote

import retrofit2.http.Body
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

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
        @Query("submitEndDate") submitEndDate: String = "",
        @Query("bookmarked") bookmarked : Boolean
    ): ContestResponse
    @GET("/region")
    suspend fun Region(): RegionResponse

    @GET("/target")
    suspend fun Target() : TargetResponse

    @GET("/contest/{id}")
    suspend fun getDetailContest(
        @Path("id") id : Int
    ):ContestDetialResponse

    @POST("/bookmark/{contestId}")
    suspend fun addBookmark(
        @Path("contestId") contestId: Int,
    ): AddBookmarkResponse

    @DELETE("/bookmark/{bookmarkId}")
    suspend fun deleteBookmark(
        @Path("bookmarkId") bookmarkId: Int
    ): DeleteBookmarkResponse

    @GET("/bookmark/list")
    suspend fun getBookmarkList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("keyword") keyword: String = ""
    ): BookmarkListResponse

    @GET("/user/me")
    suspend fun getme() : meResponse

}



