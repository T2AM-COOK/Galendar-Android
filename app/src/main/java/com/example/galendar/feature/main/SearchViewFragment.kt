package com.example.galendar.feature.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.galendar.R
import com.example.galendar.feature.contests.ContestAdapter
import com.example.galendar.feature.contests.DetailFragment
import com.example.galendar.remote.RetrofitBuilder
import com.example.galendar.databinding.FragmentSearchViewBinding
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.galendar.feature.contests.BookmarkViewModel
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels

// FilterListener 인터페이스를 구현하도록 수정
class SearchViewFragment : Fragment(R.layout.fragment_search_view), PickerFragment.FilterListener {

    private var pickerFragment: PickerFragment? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var contestAdapter: ContestAdapter
    private lateinit var binding: FragmentSearchViewBinding
    private var currentPage = 1
    private var isLoading = false
    private var hasMoreData = true
    private val pageSize = 10
    private var currentKeyword: String = ""

    private lateinit var navController: NavController
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 뷰를 설정
        binding = FragmentSearchViewBinding.inflate(inflater, container, false)

        // WindowInsetsListener 설정
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val bluelogo: ImageView = binding.bluelogo
//        bluelogo.setOnClickListener {
//            parentFragmentManager.popBackStack()
//        }

        val searchView: SearchView = binding.searchView

        // RecyclerView 설정
        recyclerView = binding.recyclerViewContests
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        contestAdapter = ContestAdapter()
        recyclerView.adapter = contestAdapter



        Log.d("SearchViewFragment", "1. onCreateView 시작")
        // 먼저 어댑터 생성
        contestAdapter = ContestAdapter()
        Log.d("SearchViewFragment", "2. 어댑터 생성")

        setupBookmarkHandling()
        loadBookmarkedItems()

        // 클릭 리스너 설정
        contestAdapter.setOnItemClickListener { contestId ->
            Log.d("SearchViewFragment", "3. 아이템 클릭: $contestId")
            val detailFragment = DetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("contestId", contestId)
                    Log.d("SearchViewFragment", "4. arguments 설정: $contestId")
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, detailFragment)
                .addToBackStack(null)
                .commit()
            Log.d("SearchViewFragment", "5. Fragment 전환 완료")
        }

        // 리사이클러뷰에 어댑터 설정
        binding.recyclerViewContests.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contestAdapter
            Log.d("SearchViewFragment", "6. 리사이클러뷰에 어댑터 설정 완료")
        }

        // 페이지네이션을 위한 스크롤 리스너 추가
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // 마지막 아이템이 보이고, 로딩 중이 아니며, 더 불러올 데이터가 있는 경우
                if (!isLoading && hasMoreData && lastVisibleItemPosition == totalItemCount - 1) {
                    loadMoreContests()
                }
            }
        })

        // SearchView 이벤트 처리
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    currentKeyword = query
                    contestAdapter.clearData() // 이전 데이터 제거
                    fetchContests(currentKeyword) // 검색 결과를 가져옴
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false // 실시간 검색 기능은 비활성화
            }
        })


        val filter: ImageView = binding.filter
        filter.setOnClickListener {
            showFilterDialog()
        }

        val nestedScrollView: NestedScrollView = binding.nestedScrollView
        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 0 && pickerFragment?.isVisible == true) {
                pickerFragment?.dismiss()
            }
        }

        return binding.root
    }
    private fun setupRecyclerView() {
        contestAdapter = ContestAdapter()
        binding.recyclerViewContests.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contestAdapter
        }
    }

    private fun showFilterDialog() {
        val pickerFragment = PickerFragment().apply {
            setFilterListener(this@SearchViewFragment)
        }
        pickerFragment.show(parentFragmentManager, "PickerFragment")
    }

    // FilterListener 인터페이스 구현
    override fun onFilterApplied(  //필터처리 검색기능
        startDate: String,
        endDate: String,
        regionIds: List<Int>,
        targetIds: List<Int>
    ) {
        // 필터가 적용되면 데이터를 새로 로드합니다
        contestAdapter.clearData()
        lifecycleScope.launch {
            try {
                val response = RetrofitBuilder.apiService.getContestList(
                    page = 1,
                    size = pageSize,
                    keyword = currentKeyword,
                    targets = targetIds,
                    regions = regionIds,
                    submitStartDate = startDate,
                    submitEndDate = endDate,
                    bookmarked = false
                )

                if (response.status == 200) {
                    if (response.data.isEmpty()) {
                        Toast.makeText(requireContext(), "필터링된 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        hasMoreData = false
                    } else {
                        contestAdapter.updateData(response.data)
                        currentPage = 1 // 페이지 초기화
                        hasMoreData = true
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "필터 적용 중 오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadBookmarkedItems()
        loadContests(isLoadMore = false) // 데이터를 새로 로드
    }

    private fun loadContests(isLoadMore: Boolean = false) {  //전체대회
        if (!isLoadMore) {
            currentPage = 1
            hasMoreData = true
        }

        if (isLoading) return
        isLoading = true

        lifecycleScope.launch {
            try {
                val response = RetrofitBuilder.apiService.getContestList(
                    page = currentPage,
                    size = pageSize,
                    keyword = currentKeyword,
                    targets = emptyList(),
                    regions = emptyList(),
                    submitStartDate = "",
                    submitEndDate = "",
                    bookmarked = false
                )

                if (response.status == 200) {
                    if (response.data.isEmpty()) {
                        hasMoreData = false
                    } else {
                        if (isLoadMore) {
                            contestAdapter.addData(response.data)
                        } else {
                            contestAdapter.updateData(response.data)
                        }
                    }
                } else {
                    Log.e("loadContests", "Error: ${response.message}")
                    hasMoreData = false
                }
            } catch (e: Exception) {
                Log.e("loadContests", "Exception: ${e.message}")
                hasMoreData = false
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadMoreContests() {
        currentPage++
        loadContests(isLoadMore = true)
    }

    private fun setupBookmarkHandling() {
        // 북마크 클릭 리스너 설정
        contestAdapter.setOnBookmarkClickListener { contestId, isBookmarking ->
            Log.d("SearchViewFragment", "북마크 클릭: contestId=$contestId, isBookmarking=$isBookmarking")

            lifecycleScope.launch {
                try {
                    if (isBookmarking) {
                        // 북마크 추가
                        val response = RetrofitBuilder.apiService.addBookmark(contestId)
                        Log.d("SearchViewFragment", "북마크 추가 응답: ${response.status}")

                        if (response.status == 200) {
                            contestAdapter.updateBookmarkStatus(contestId, true)
                            Toast.makeText(requireContext(), "북마크가 추가되었습니다", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // 먼저 북마크 ID를 찾기 위해 북마크 목록을 가져옴
                        val bookmarkList = RetrofitBuilder.apiService.getBookmarkList(1, 100).data
                        val bookmarkId = bookmarkList.find { it.contestId == contestId }?.id

                        if (bookmarkId != null) {
                            val response = RetrofitBuilder.apiService.deleteBookmark(bookmarkId)
                            Log.d("SearchViewFragment", "북마크 삭제 응답: ${response.status}")

                            if (response.status == 200) {
                                contestAdapter.updateBookmarkStatus(contestId, false)
                                Toast.makeText(requireContext(), "북마크가 삭제되었습니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SearchViewFragment", "북마크 처리 실패: ${e.message}")
                    Toast.makeText(requireContext(), "북마크 처리 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 초기 북마크 상태 로드
    private fun loadBookmarkedItems() {
        lifecycleScope.launch {
            try {
                val bookmarkList = RetrofitBuilder.apiService.getBookmarkList(1, 100).data
                Log.d("SearchViewFragment", "북마크 목록 로드됨: ${bookmarkList.size}개")
                bookmarkList.forEach { bookmark ->
                    contestAdapter.updateBookmarkStatus(bookmark.contestId, true)
                }
            } catch (e: Exception) {
                Log.e("SearchViewFragment", "북마크 목록 로드 실패: ${e.message}")
            }
        }
    }

    private fun fetchContests(keyword: String) {  //키워드 검색 기능
        currentPage = 1  // 검색 시 페이지 초기화
        hasMoreData = true  // 검색 시 데이터 존재 여부 초기화

        lifecycleScope.launch {
            try {
                isLoading = true

                val response = RetrofitBuilder.apiService.getContestList(
                    page = currentPage,
                    size = pageSize,
                    keyword = keyword,
                    bookmarked = false
                )

                if (response.status == 200) {
                    if (response.data.isEmpty()) {
                        Toast.makeText(requireContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        hasMoreData = false
                    } else {
                        contestAdapter.updateData(response.data)
                    }
                } else {
                    Toast.makeText(requireContext(), "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    hasMoreData = false
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                hasMoreData = false
            } finally {
                isLoading = false
            }
        }
    }
}
