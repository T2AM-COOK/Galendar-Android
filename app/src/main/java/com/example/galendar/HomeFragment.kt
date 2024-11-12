package com.example.galendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.util.Locale
import java.util.Calendar
import java.text.SimpleDateFormat
import android.content.res.Configuration
import android.widget.TextView

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 한국어 로케일 설정
        setLocale("ko")

        // MaterialCalendarView 가져오기
        val materialCalendarView = view.findViewById<MaterialCalendarView>(R.id.materialCalendarView)
        val dateTextView : TextView = view.findViewById(R.id.date)

        materialCalendarView.setTileHeight(130)

        // 오늘 날짜 표시
        updateDateText(dateTextView, CalendarDay.today())

        // 날짜 선택 리스너 추가
        val selectedDecorator = SelectedDecorator()  // 선택된 날짜 데코레이터 추가
        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                updateDateText(dateTextView, date)  // 선택된 날짜에 따라 텍스트 업데이트
                selectedDecorator.setSelectedDay(date) // 선택된 날짜 설정
                widget.invalidateDecorators() // 데코레이터 다시 적용
            }
        }

        // 일요일과 토요일 데코레이터 추가
        materialCalendarView.addDecorator(SundayDecorator())
        materialCalendarView.addDecorator(SaturdayDecorator())
        materialCalendarView.addDecorator(selectedDecorator) // 선택된 날짜 데코레이터 적용

        // 텍스트 스타일 적용
        materialCalendarView.setHeaderTextAppearance(R.style.CustomCalendarHeaderText)
        materialCalendarView.setDateTextAppearance(R.style.CustomTextAppearanceDayClicked)

        // SearchView 클릭 시, SearchActivity로 이동
        val searchView: SearchView = view.findViewById(R.id.searchView)
        searchView.setOnSearchClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }

        // WindowInsets 설정
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
        // CalendarDay를 Date로 변환하여 SimpleDateFormat에서 사용할 수 있게 설정
        val calendar = Calendar.getInstance()
        calendar.set(date.year, date.month - 1, date.day) // month는 0부터 시작하므로 -1
        val dateFormat = SimpleDateFormat("M월 d일", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        dateTextView.text = "$formattedDate 접수마감 대회"
    }
}
