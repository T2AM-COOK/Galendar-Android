package com.example.galendar

import android.os.Bundle
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.SearchView
import android.content.Intent

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        // WindowInsetsListener 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            return@setOnApplyWindowInsetsListener insets
        }

        val bluelogo: ImageView = findViewById(R.id.bluelogo)

        // ImageView 클릭 리스너 설정
        bluelogo.setOnClickListener {
            // 클릭 시 HomeActivity로 이동하는 인텐트 생성
            val intent = Intent(this, HomeFragment::class.java)
            startActivity(intent)
        }

        // SearchView 초기화
        val searchView: SearchView = findViewById(R.id.searchView)

        // SearchView가 활성화될 때 바로 검색창을 열고 키보드도 띄우기
        searchView.requestFocus()
        searchView.isIconified = false

        // SearchView에서 입력된 검색어를 받아오기
        val query = intent.getStringExtra("searchQuery") ?: ""
        searchView.setQuery(query, false)

        // 키보드를 자동으로 열기
        searchView.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }, 200)  // 200ms 지연 후 실행

        // SearchView에서 입력된 검색어를 처리
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색어 제출 시 처리
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어 변경 시 처리
                return false
            }
        })
    }
}
