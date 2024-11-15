package com.example.galendar

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar
import java.util.Locale
import android.view.ViewGroup
import android.widget.FrameLayout
import android.view.WindowManager
import android.view.Gravity


class PickerFragment : BottomSheetDialogFragment(R.layout.fragment_picker) {

    override fun onStart() {
        super.onStart()

        // BottomSheet의 다이얼로그 인스턴스를 가져옴
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            // 전체 화면 크기로 설정
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, // 너비
                ViewGroup.LayoutParams.MATCH_PARENT  // 높이
            )

            // 바텀시트를 화면의 아래쪽으로 붙이기
            window?.setGravity(Gravity.BOTTOM)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view.findViewById를 통해 버튼 참조
        val submitStartData: Button = view.findViewById(R.id.submit_start_data)
        val submitEndData: Button = view.findViewById(R.id.submit_end_data)
        val contestStartData: Button = view.findViewById(R.id.contest_start_data)
        val contestEndData: Button = view.findViewById(R.id.contest_end_data)

        fun showDataPicker(button : Button) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // 선택된 날짜를 "YYYY년 MM월 DD일" 형식으로 버튼 텍스트에 설정
                val formattedDate = String.format(Locale.getDefault(), "%d년 %02d월 %02d일", selectedYear, selectedMonth + 1, selectedDay)
                button.text = formattedDate
            }, year, month, day).show()
        }

        submitStartData.setOnClickListener { showDataPicker(submitStartData) }
        submitEndData.setOnClickListener { showDataPicker(submitEndData) }
        contestStartData.setOnClickListener { showDataPicker(contestStartData) }
        contestEndData.setOnClickListener { showDataPicker(contestEndData) }

        // WindowInsets 설정
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.picker_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
