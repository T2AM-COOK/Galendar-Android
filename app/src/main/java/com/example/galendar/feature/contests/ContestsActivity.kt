//package com.example.galendar
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class ContestsActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var contestAdapter: ContestAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_search_view)
//
//        // RecyclerView 설정
//        recyclerView = findViewById(R.id.recyclerViewContests)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        // Retrofit을 통해 서버에서 대회 데이터를 가져옵니다
//        loadContests()
//    }
//
//    private fun loadContests() {
//        // Request 데이터를 설정 (페이지, 크기 등)
//        val contestRequest = ContestRequest(
//            page = 1,  // 기본값 1
//            size = 20, // 기본값 20
//            keyword = "",  // 검색어가 필요 없다면 빈 문자열
//            targets = emptyList(),
//            regions = emptyList(),
//            submitStartDate = "",
//            submitEndDate = ""
//        )
//
//        // Retrofit을 사용하여 서버에서 대회 데이터를 받아옵니다.
//        val contestApiService = RetrofitClient.contestService
//        contestApiService.getContestList(
//            contestRequest.page,
//            contestRequest.size,
//            contestRequest.keyword,
//            contestRequest.targets,
//            contestRequest.regions,
//            contestRequest.submitStartDate,
//            contestRequest.submitEndDate
//        ).enqueue(object : Callback<ContestResponse> {
//            override fun onResponse(call: Call<ContestResponse>, response: Response<ContestResponse>) {
//                if (response.isSuccessful) {
//                    val contests = response.body()?.data
//                    contests?.let {
//                        // 받아온 대회 데이터를 어댑터에 설정하여 RecyclerView에 표시
//                        contestAdapter = ContestAdapter(it)
//                        recyclerView.adapter = contestAdapter
//                    }
//                } else {
//                    Toast.makeText(this@ContestsActivity, "서버 오류 발생", Toast.LENGTH_SHORT).show()
//                    Log.e("ContestsActivity", "서버 오류 발생: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<ContestResponse>, t: Throwable) {
//                Toast.makeText(this@ContestsActivity, "통신 실패: ${t.message}", Toast.LENGTH_SHORT).show()
//                Log.e("ContestsActivity", "통신 실패: ${t.message}")
//            }
//        })
//    }
//}
