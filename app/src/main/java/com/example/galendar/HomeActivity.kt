package com.example.galendar

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Locale
import android.content.res.Configuration
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import androidx.appcompat.widget.SearchView


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 한국어 로케일 설정
        setLocale("ko")

        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // MaterialCalendarView를 가져옴
        val materialCalendarView = findViewById<MaterialCalendarView>(R.id.materialCalendarView)

        // 일요일과 토요일 데코레이터 추가
        materialCalendarView.addDecorator(SundayDecorator())
        materialCalendarView.addDecorator(SaturdayDecorator())

        // 선택된 날짜 데코레이터 추가
        val selectedDecorator = SelectedDecorator()
        materialCalendarView.addDecorator(selectedDecorator)

        // 텍스트 스타일 적용
        materialCalendarView.setHeaderTextAppearance(R.style.CustomCalendarHeaderText)
        materialCalendarView.setDateTextAppearance(R.style.CustomTextAppearanceDayClicked)

        // 날짜 선택 리스너 추가
        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                selectedDecorator.setSelectedDay(date)  // 선택된 날짜 설정
                widget.invalidateDecorators()            // 데코레이터 다시 적용
            }
        }

//        val searchView: SearchView = findViewById(R.id.searchView)



        // WindowInsets 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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
}
