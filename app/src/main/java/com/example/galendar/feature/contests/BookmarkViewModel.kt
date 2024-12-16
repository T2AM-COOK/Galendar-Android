package com.example.galendar.feature.contests

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galendar.remote.RetrofitBuilder
import kotlinx.coroutines.launch
import com.example.galendar.remote.BookmarkList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.asSharedFlow




// BookmarkViewModel 수정
class BookmarkViewModel : ViewModel() {
    private val _bookmarkList = MutableLiveData<List<BookmarkList>>()
    val bookmarkList: LiveData<List<BookmarkList>> get() = _bookmarkList

    // 북마크 상태 변경을 추적하기 위한 Flow
    private val _bookmarkStateFlow = MutableSharedFlow<Pair<Int, Boolean>>()
    val bookmarkStateFlow = _bookmarkStateFlow.asSharedFlow()

    fun updateBookmarkState(contestId: Int, isBookmarked: Boolean) {
        viewModelScope.launch {
            _bookmarkStateFlow.emit(Pair(contestId, isBookmarked))
        }
    }

    fun fetchBookmarks() {
        viewModelScope.launch {
            try {
                val response = RetrofitBuilder.apiService.getBookmarkList(page = 1, size = 40)
                if (response.status == 200) {
                    _bookmarkList.value = response.data
                    Log.d("BookmarkViewModel", "Fetch bookmarks success: ${response.data.size} items")
                }
            } catch (e: Exception) {
                Log.e("BookmarkViewModel", "Fetch bookmarks failed: ${e.message}")
            }
        }
    }

    fun addBookmark(contestId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitBuilder.apiService.addBookmark(contestId)
                if (response.status == 200) {
                    updateBookmarkState(contestId, true)
                    fetchBookmarks()  // 북마크 리스트 갱신
                }
            } catch (e: Exception) {
                Log.e("BookmarkViewModel", "Add bookmark failed: ${e.message}")
            }
        }
    }

    fun deleteBookmark(bookmarkId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitBuilder.apiService.deleteBookmark(bookmarkId)
                if (response.status == 200) {
                    val contestId = bookmarkList.value?.find { it.id == bookmarkId }?.contestId
                    contestId?.let { updateBookmarkState(it, false) }
                    fetchBookmarks()  // 북마크 리스트 갱신
                }
            } catch (e: Exception) {
                Log.e("BookmarkViewModel", "Delete bookmark failed: ${e.message}")
            }
        }
    }
}