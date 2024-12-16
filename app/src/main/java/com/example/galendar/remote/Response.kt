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
    val contestEndDate: String?,
    val bookmarked : Boolean
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

data class ContestDetialResponse(
    val status: Int,
    val message: String,
    val data : DetailData
)
data class DetailData(
    val id: Int,
    val title: String,
    val content: String,
    val cost: String,
    val link: String,
    val imgLink: String,
    val submitStartDate: String,
    val submitEndDate: String,
    val contestStartDate: String?,
    val contestEndDate: String?,
    val targets: List<DetailTarget>,
    val regions: List<DetailRegion>,
    val bookmarked: Boolean
)
data class DetailTarget(
    val id: Int,
    val name: String
)

data class DetailRegion(
    val id: Int,
    val name: String
)

data class AddBookmarkResponse(
    val status: Int,     // 응답 상태 코드 (e.g., 200)
    val message: String  // 응답 메시지 (e.g., "북마크가 등록되었습니다.")
)
data class BookmarkListResponse(
    val status: Int,
    val message: String,
    val data : List<BookmarkList>
)
 data class BookmarkList(
     val id: Int, //bookmarkId
     val contestId : Int,
     val title: String,
     val link: String,
     val imgLink: String,
     val submitStartDate: String,
     val submitEndDate: String,
     val contestStartDate: String?,
     val contestEndDate: String?
 )
data class DeleteBookmarkResponse(
    val status: Int,
    val message: String
)

data class  meResponse(
    val status: Int,
    val message: String,
    val data : MeList
)
data class MeList(
    val id : Int,
    val email : String,
    val name : String,
    val role : String
)