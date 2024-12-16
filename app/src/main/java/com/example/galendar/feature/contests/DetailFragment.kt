package com.example.galendar.feature.contests

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.galendar.R
import com.example.galendar.databinding.FragmentDetailBinding
import com.example.galendar.remote.DetailData
import com.example.galendar.remote.RetrofitBuilder
import kotlinx.coroutines.launch

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()
    private var isBookmarked = false
    private var contestId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contestId = arguments?.getInt("contestId")
        Log.d("DetailFragment", "전달받은 대회 ID: $contestId")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        contestId?.let { id ->
            fetchContestDetails(id)
            checkBookmarkStatus(id)
            setupBookmarkButton(id)

            lifecycleScope.launch {
                bookmarkViewModel.bookmarkStateFlow.collect { (updatedContestId, bookmarkState) ->
                    if (id == updatedContestId) {
                        _binding?.let { updateBookmarkUI(bookmarkState) }
                    }
                }
            }
        } ?: showError("대회 정보를 찾을 수 없습니다.")
    }

    private fun checkBookmarkStatus(contestId: Int) {
        lifecycleScope.launch {
            try {
                val bookmarks = bookmarkViewModel.bookmarkList.value
                isBookmarked = bookmarks?.any { it.contestId == contestId } == true
                _binding?.let { updateBookmarkUI(isBookmarked) }
            } catch (e: Exception) {
                Log.e("DetailFragment", "북마크 상태 확인 실패: ${e.message}")
            }
        }
    }

    private fun setupBookmarkButton(contestId: Int) {
        _binding?.bookmarkHeart?.setOnClickListener {
            lifecycleScope.launch {
                try {
                    if (!isBookmarked) {
                        val response = RetrofitBuilder.apiService.addBookmark(contestId)
                        if (response.status == 200) {
                            isBookmarked = true
                            _binding?.let { updateBookmarkUI(true) }
                            bookmarkViewModel.fetchBookmarks()
                            showToast("북마크가 추가되었습니다")
                        }
                    } else {
                        val bookmarks = bookmarkViewModel.bookmarkList.value
                        val bookmarkId = bookmarks?.find { it.contestId == contestId }?.id
                        if (bookmarkId != null) {
                            val response = RetrofitBuilder.apiService.deleteBookmark(bookmarkId)
                            if (response.status == 200) {
                                isBookmarked = false
                                _binding?.let { updateBookmarkUI(false) }
                                bookmarkViewModel.fetchBookmarks()
                                showToast("북마크가 삭제되었습니다")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("DetailFragment", "북마크 처리 실패: ${e.message}")
                    showError("북마크 처리 중 오류가 발생했습니다")
                }
            }
        }
    }

    private fun updateBookmarkUI(isBookmarked: Boolean) {
        _binding?.bookmarkHeart?.setImageResource(
            if (isBookmarked) R.drawable.bookmarkheart_filled
            else R.drawable.bookmarkheart
        )
    }

    private fun fetchContestDetails(id: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitBuilder.apiService.getDetailContest(id)
                if (response.status == 200 && response.data != null) {
                    _binding?.let { updateUI(response.data) }
                } else {
                    showError("데이터를 가져올 수 없습니다.")
                }
            } catch (e: Exception) {
                showError("오류가 발생했습니다: ${e.message}")
                Log.d("DetailFragment", "${e.message}")
            }
        }
    }

    private fun updateUI(data: DetailData) {
        _binding?.let { binding ->
            with(binding) {
                title.text = data.title
                content.text = data.content
                submitDate.text = "${data.submitStartDate} ~ ${data.submitEndDate}"
                contestDate.text = data.contestStartDate?.let {
                    "$it ~ ${data.contestEndDate}"
                } ?: "대회 일정 미정"
                target.text = data.targets.joinToString(", ") { it.name }
                region.text = data.regions.joinToString(", ") { it.name }
                cost.text = data.cost

                context?.let { ctx ->
                    Glide.with(ctx)
                        .load(data.imgLink)
                        .into(img)
                }

                viewMore.setOnClickListener {
                    val isExpanded = content.maxLines != 3
                    content.maxLines = if (isExpanded) 3 else Int.MAX_VALUE
                    viewMore.text = if (isExpanded) "더보기" else "접기"
                }

                img.setOnClickListener {
                    data.link?.let { url ->
                        startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url)))
                    } ?: showError("유효한 링크가 없습니다.")
                }

                shareBtn.setOnClickListener {
                    val shareIntent = Intent.createChooser(Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, data.link)
                        putExtra(Intent.EXTRA_TITLE, data.title)
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }, null)
                    startActivity(shareIntent)
                }
            }
        }
    }

    private fun showToast(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError(message: String) {
        showToast(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}