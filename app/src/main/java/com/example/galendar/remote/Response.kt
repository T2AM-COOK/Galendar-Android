package com.example.galendar.remote

data class SignUpResponse(
    val status: Int,
    val message: String,
    val data: Any?
)

data class LoginResponse(
    val status: Int,
    val message: String,
    val data: LoginData?
)
data class LoginData(
    val accessToken: String,
    val refreshToken: String
)
data class RefreshResponse(
    val status: Int,
    val message: String,
    val data: RefreshData?
)
data class RefreshData(
    val token: String,
    val refreshToken: String
)
data class VerifyResponse(
    val status: Int,
    val message: String,
    val data: Any?
)
data class SendEmailResponse(
    val status: Int,
    val message: String,
    val data: Any?
)
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
    val contestStartDate: String?,
    val contestEndDate: String?
)
data class RegionResponse(
    val status: Int,
    val message: String,
    val data: List<RegionData>
)
data class RegionData(
    val id: Int,
    val name: String
)
data class TargetResponse(
    val status: Int,
    val message: String,
    val data: List<TargetData>
)
data class TargetData(
    val id: Int,
    val name: String
)