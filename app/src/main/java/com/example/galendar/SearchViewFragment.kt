package com.example.galendar

import android.os.Bundle
import android.util.Log
import android.view.View
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewFragment : Fragment(R.layout.fragment_search_view) {

    private var pickerFragment: PickerFragment? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var contestAdapter: ContestAdapter
    private var contestList: List<ContestData> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // WindowInsetsListener 설정
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.search_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bluelogo: ImageView = view.findViewById(R.id.bluelogo)
        bluelogo.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val searchView: SearchView = view.findViewById(R.id.searchView)

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.recyclerViewContests)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 초기 데이터 설정
        contestAdapter = ContestAdapter(contestList)
        recyclerView.adapter = contestAdapter

        // 전체 데이터 로드
        loadContests()

        // SearchView 이벤트 처리
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    fetchContests(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val filter: ImageView = view.findViewById(R.id.filter)
        filter.setOnClickListener {
            if (pickerFragment == null) {
                pickerFragment = PickerFragment()
            }
            pickerFragment?.let {
                it.show(parentFragmentManager, it.tag)
            }
        }

        val nestedScrollView: NestedScrollView = view.findViewById(R.id.nested_scroll_view)
        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 0 && pickerFragment?.isVisible == true) {
                pickerFragment?.dismiss()
            }
        }
    }

    private fun fetchContests(keyword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://3.37.189.59/") // 서버 주소
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val contestService = retrofit.create(ContestService::class.java)

                val response = contestService.getContestList(
                    page = 1,
                    size = 10,
                    keyword = keyword
                )

                withContext(Dispatchers.Main) {
                    if (response.status == 200 && response.data.isNotEmpty()) {
                        contestAdapter.updateData(response.data)
                    } else {
                        Toast.makeText(requireContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "데이터를 가져오지 못했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadContests() {
        lifecycleScope.launch {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://3.37.189.59/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val contestService = retrofit.create(ContestService::class.java)

                val response = contestService.getContestList(
                    page = 1,
                    size = 20,
                    keyword = "",
                    targets = emptyList(),
                    regions = emptyList(),
                    submitStartDate = "",
                    submitEndDate = ""
                )

                if (response.status == 200) {
                    contestAdapter.updateData(response.data)
                } else {
                    Log.e("loadContests", "Error: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("loadContests", "Exception: ${e.message}")
            }
        }
    }
}
