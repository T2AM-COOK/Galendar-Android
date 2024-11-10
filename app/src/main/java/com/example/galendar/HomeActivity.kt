//package com.example.galendar
//
//import android.content.Intent
//import android.graphics.Color
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.prolificinteractive.materialcalendarview.MaterialCalendarView
//import com.google.android.material.tabs.TabLayout
//import androidx.appcompat.widget.SearchView
//import android.util.Log
//import java.util.Locale
//import android.content.res.Configuration
//import com.google.android.material.bottomnavigation.BottomNavigationView
//
//class HomeActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // 한국어 로케일 설정
//        setLocale("ko")
//
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_home)
//
//        // MaterialCalendarView를 가져옴
//        val materialCalendarView = findViewById<MaterialCalendarView>(R.id.materialCalendarView)
//
//        // 일요일과 토요일 데코레이터 추가
//        materialCalendarView.addDecorator(SundayDecorator())
//        materialCalendarView.addDecorator(SaturdayDecorator())
//
//        // 선택된 날짜 데코레이터 추가
//        val selectedDecorator = SelectedDecorator()
//        materialCalendarView.addDecorator(selectedDecorator)
//
//        // 텍스트 스타일 적용
//        materialCalendarView.setHeaderTextAppearance(R.style.CustomCalendarHeaderText)
//        materialCalendarView.setDateTextAppearance(R.style.CustomTextAppearanceDayClicked)
//
//        // 날짜 선택 리스너 추가
//        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
//            if (selected) {
//                selectedDecorator.setSelectedDay(date)  // 선택된 날짜 설정
//                widget.invalidateDecorators()            // 데코레이터 다시 적용
//            }
//        }
//
//        // SearchView 클릭 시, SearchActivity로 이동
//        val searchView: SearchView = findViewById(R.id.searchView)
//        searchView.setOnSearchClickListener {
//            val intent = Intent(this, SearchActivity::class.java)
//            startActivity(intent)
//        }
//
//        // WindowInsets 설정
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        // BottomNavigationView 설정
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//        bottomNavigationView.selectedItemId = R.id.navigation_home  // 현재 페이지로 "Home" 선택
//
//        bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_home -> true  // 현재 페이지이므로 아무 작업도 하지 않음
//                R.id.navigation_bookmark -> {
//                    startActivity(Intent(this, BookmarkActivity::class.java))  // 북마크 페이지로 이동
//                    true
//                }
//                R.id.navigation_profile -> {
//                    startActivity(Intent(this, ProfileActivity::class.java))  // 프로필 페이지로 이동
//                    true
//                }
//                else -> false
//            }
//        }
//    }
//
//    private fun setLocale(languageCode: String) {
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//        val config = Configuration(resources.configuration)
//        config.setLocale(locale)
//        resources.updateConfiguration(config, resources.displayMetrics)
//    }
//}
