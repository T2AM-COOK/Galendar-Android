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
import com.example.galendar.remote.RetrofitBuilder
import com.example.galendar.databinding.FragmentSearchViewBinding
import kotlinx.coroutines.launch

class SearchViewFragment : Fragment(R.layout.fragment_search_view) {

    private var pickerFragment: PickerFragment? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var contestAdapter: ContestAdapter
    private lateinit var binding : FragmentSearchViewBinding
    private var currentPage = 1
    private var isLoading = false
    private var hasMoreData = true
    private val pageSize = 20
    private var currentKeyword: String = ""

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

        val bluelogo: ImageView = binding.bluelogo
        bluelogo.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val searchView: SearchView = binding.searchView

        // RecyclerView 설정
        recyclerView = binding.recyclerViewContests
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        contestAdapter = ContestAdapter()
        recyclerView.adapter = contestAdapter

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
            if (pickerFragment == null) {
                pickerFragment = PickerFragment()
            }
            pickerFragment?.let {
                it.show(parentFragmentManager, it.tag)
            }
        }

        val nestedScrollView: NestedScrollView = binding.nestedScrollView
        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 0 && pickerFragment?.isVisible == true) {
                pickerFragment?.dismiss()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadContests(isLoadMore = false) // 데이터를 새로 로드
    }


    private fun loadContests(isLoadMore: Boolean = false) {
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
                    keyword = currentKeyword, // 검색어가 있을 경우 searchView.query.toString()
                    targets = emptyList(),
                    regions = emptyList(),
                    submitStartDate = "",
                    submitEndDate = ""
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

    private fun fetchContests(keyword: String) {
        currentPage = 1  // 검색 시 페이지 초기화
        hasMoreData = true  // 검색 시 데이터 존재 여부 초기화

        lifecycleScope.launch {
            try {
                isLoading = true

                val response = RetrofitBuilder.apiService.getContestList(
                    page = currentPage,
                    size = pageSize,
                    keyword = keyword
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
