package com.example.galendar.feature.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.galendar.R
import com.example.galendar.databinding.ActivityMainBinding
import com.example.galendar.feature.contests.BookmarkViewModel
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // 프래그먼트 인스턴스들
    private var homeFragment: HomeFragment? = null
    private var bookmarkFragment: BookmarkFragment? = null
    private var profileFragment: ProfileFragment? = null
    private var searchViewFragment: SearchViewFragment? = null
    private val bookmarkViewModel: BookmarkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setBottomNavigationView()

        // 앱 초기 실행 시 홈 화면으로 설정
        if (savedInstanceState == null) {
            homeFragment = HomeFragment() // 홈 프래그먼트 인스턴스 생성
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container, homeFragment!!, "HOME") // 처음에 홈 프래그먼트 추가
                .commit()

            // 네비게이션 바에서 홈 아이템을 선택된 상태로 설정
            binding.bottomNavigationView.selectedItemId = R.id.navigation_home
        }
    }

    private fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val transaction = supportFragmentManager.beginTransaction()

            when (item.itemId) {
                R.id.navigation_home -> {
                    // 홈 프래그먼트가 null일 경우에만 새로 생성
                    if (homeFragment == null) {
                        homeFragment = HomeFragment()
                    }
                    transaction.replace(R.id.main_container, homeFragment!!)
                }
                R.id.navigation_search -> {
                    // 서치 프래그먼트가 null일 경우에만 새로 생성
                    if (searchViewFragment == null) {
                        searchViewFragment = SearchViewFragment()
                    }
                    transaction.replace(R.id.main_container, searchViewFragment!!)
                }
                R.id.navigation_bookmark -> {
                    // 북마크 프래그먼트가 null일 경우에만 새로 생성
                    if (bookmarkFragment == null) {
                        bookmarkFragment = BookmarkFragment()
                    }
                    transaction.replace(R.id.main_container, bookmarkFragment!!)
                }
                R.id.navigation_profile -> {
                    // 프로필 프래그먼트가 null일 경우에만 새로 생성
                    if (profileFragment == null) {
                        profileFragment = ProfileFragment()
                    }
                    transaction.replace(R.id.main_container, profileFragment!!)
                }
            }

            // 트랜잭션을 커밋하면서 이전 상태를 백스택에 추가하지 않음 (필요시 추가할 수 있음)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()

            true
        }
    }
}
