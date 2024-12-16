package com.example.galendar.feature.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.galendar.R
import com.example.galendar.databinding.FragmentBookmarkBinding
import com.example.galendar.feature.contests.BookmarkViewModel
import com.example.galendar.feature.contests.ContestAdapter
import com.example.galendar.remote.ContestData
import kotlinx.coroutines.launch
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import com.example.galendar.feature.contests.DetailFragment

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by activityViewModels()
    private lateinit var contestAdapter: ContestAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeBookmarks()
        viewModel.fetchBookmarks()
    }

    private fun setupRecyclerView() {
        contestAdapter = ContestAdapter().apply {
            setOnBookmarkClickListener { contestId, isBookmarked ->
                lifecycleScope.launch {
                    try {
                        if (!isBookmarked) {
                            val bookmarkList = viewModel.bookmarkList.value
                            val bookmarkId = bookmarkList?.find { it.contestId == contestId }?.id
                            if (bookmarkId != null) {
                                viewModel.deleteBookmark(bookmarkId)
                                Log.d("BookmarkFragment", "북마크 삭제: bookmarkId=$bookmarkId")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("BookmarkFragment", "북마크 처리 실패: ${e.message}")
                        Toast.makeText(context, "북마크 처리 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            setOnItemClickListener { contestId ->
                Log.d("BookmarkFragment", "아이템 클릭: $contestId")
                val detailFragment = DetailFragment().apply {
                    arguments = Bundle().apply {
                        putInt("contestId", contestId)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.bookmarkRecycler.apply {
            adapter = contestAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeBookmarks() {
        viewModel.bookmarkList.observe(viewLifecycleOwner) { bookmarks ->
            Log.d("BookmarkFragment", "북마크 데이터 업데이트: ${bookmarks.size}개")
            contestAdapter.updateData(bookmarks)

            bookmarks.forEach { bookmark ->
                contestAdapter.updateBookmarkStatus(bookmark.contestId, true)
            }

            binding.apply {
                if (bookmarks.isEmpty()) {
                    emptyStateLayout.visibility = View.VISIBLE
                    bookmarkRecycler.visibility = View.GONE
                } else {
                    emptyStateLayout.visibility = View.GONE
                    bookmarkRecycler.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}