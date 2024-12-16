package com.example.galendar.feature.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.galendar.R
import com.example.galendar.feature.contests.CalendarAdapter
import com.example.galendar.remote.RetrofitBuilder
import com.example.galendar.util.DotDecorator
import com.example.galendar.util.SaturdayDecorator
import com.example.galendar.util.SelectedDecorator
import com.example.galendar.util.SundayDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var materialCalendarView: MaterialCalendarView
    private lateinit var dotDecorator: DotDecorator
    private var selectedDate: String? = null
    private lateinit var selectedDecorator: SelectedDecorator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setLocale("ko")

        materialCalendarView = view.findViewById(R.id.materialCalendarView)
        val dateTextView: TextView = view.findViewById(R.id.date)

        materialCalendarView.setTileHeight(130)

        // 초기 닷 데코레이터 설정
        dotDecorator = DotDecorator(requireContext(), emptySet())

        // 오늘 날짜 표시 및 초기 데이터 로드
        val today = CalendarDay.today()
        updateDateText(dateTextView, today)

        // 모든 북마크 데이터를 가져와서 점을 표시할 날짜들을 설정
        fetchAllBookmarks()

        // 날짜 선택 리스너 추가
        val selectedDecorator = SelectedDecorator()
        dotDecorator = DotDecorator(requireContext(), emptySet())
        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                updateDateText(dateTextView, date)
                selectedDecorator.setSelectedDay(date)

                // DotDecorator 업데이트
                widget.removeDecorator(dotDecorator)
                dotDecorator = DotDecorator(requireContext(), dotDecorator.dates, date)
                widget.addDecorator(dotDecorator)

                widget.invalidateDecorators()
                fetchBookmarksForDate(date)
            }
        }

        // 데코레이터 추가
        materialCalendarView.apply {
            addDecorator(SundayDecorator())
            addDecorator(SaturdayDecorator())
            addDecorator(dotDecorator)
            addDecorator(selectedDecorator)
            setHeaderTextAppearance(R.style.CustomCalendarHeaderText)
            setDateTextAppearance(R.style.CustomTextAppearanceDayClicked)
        }

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        calendarAdapter = CalendarAdapter()
        view?.findViewById<RecyclerView>(R.id.calendarRecycler)?.apply {
            adapter = calendarAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun fetchBookmarksForDate(date: CalendarDay) {
        // CalendarDay를 "YYYY-MM-DD" 형식의 문자열로 변환
        val dateStr = String.format("%04d-%02d-%02d", date.year, date.month, date.day)

        lifecycleScope.launch {
            try {
                val response = RetrofitBuilder.apiService.getBookmarkList(page = 1, size = 100)
                if (response.status == 200) {
                    val filteredBookmarks = response.data.filter { bookmark ->
                        bookmark.submitEndDate.substring(0, 10) == dateStr
                    }
                    calendarAdapter.submitList(filteredBookmarks)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "북마크를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchAllBookmarks() {
        lifecycleScope.launch {
            try {
                val response = RetrofitBuilder.apiService.getBookmarkList(page = 1, size = 100)
                if (response.status == 200) {
                    // 북마크가 있는 날짜들을 CalendarDay 세트로 변환
                    val datesWithEvents = response.data.map { bookmark ->
                        val dateParts = bookmark.submitEndDate.substring(0, 10).split("-")
                        CalendarDay.from(
                            dateParts[0].toInt(),
                            dateParts[1].toInt(),
                            dateParts[2].toInt()
                        )
                    }.toSet()

                    // 새로운 dotDecorator 생성 및 적용
                    dotDecorator = DotDecorator(requireContext(), datesWithEvents)
                    materialCalendarView.removeDecorator(dotDecorator) // 기존 데코레이터 제거
                    materialCalendarView.addDecorator(dotDecorator)    // 새 데코레이터 추가

                    // 오늘 날짜의 북마크 표시
                    fetchBookmarksForDate(CalendarDay.today())
                }
            } catch (e: Exception) {
                Toast.makeText(context, "데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun updateDateText(dateTextView: TextView, date: CalendarDay) {
        val calendar = Calendar.getInstance()
        calendar.set(date.year, date.month - 1, date.day)
        val dateFormat = SimpleDateFormat("M월 d일", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        dateTextView.text = "$formattedDate 접수마감 대회"
    }
}