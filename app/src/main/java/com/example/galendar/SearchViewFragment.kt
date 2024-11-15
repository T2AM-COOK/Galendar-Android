package com.example.galendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.widget.NestedScrollView

class SearchViewFragment : Fragment() {

    private var pickerFragment: PickerFragment? = null  // pickerFragment를 멤버 변수로 선언

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_search_view 레이아웃을 inflate
        return inflater.inflate(R.layout.fragment_search_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // WindowInsetsListener 설정
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.search_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bluelogo: ImageView = view.findViewById(R.id.bluelogo)

        // ImageView 클릭 리스너 설정
        bluelogo.setOnClickListener {
            // 클릭 시 HomeFragment로 돌아가는 처리
            parentFragmentManager.popBackStack()
        }

        // SearchView 초기화
        val searchView: SearchView = view.findViewById(R.id.searchView)

        // SearchView가 활성화될 때 바로 검색창을 열고 키보드도 띄우기
        searchView.requestFocus()
        searchView.isIconified = false

        // SearchView에서 입력된 검색어를 받아오기
        val query = activity?.intent?.getStringExtra("searchQuery") ?: ""
        searchView.setQuery(query, false)

        // 키보드를 자동으로 열기
        searchView.postDelayed({
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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

        val filter: ImageView = view.findViewById(R.id.filter)
        filter.setOnClickListener {
            // pickerFragment가 null일 경우만 초기화
            if (pickerFragment == null) {
                pickerFragment = PickerFragment()
            }
            pickerFragment?.let {
                it.show(parentFragmentManager, it.tag)
            }
        }


        val nestedScrollView: NestedScrollView = view.findViewById(R.id.nested_scroll_view)

        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            // 스크롤이 내려가면 PickerFragment(즉, BottomSheet)를 닫음
            if (scrollY > 0 && pickerFragment?.isVisible == true) {
                pickerFragment?.dismiss()
            }
        }
    }
}
