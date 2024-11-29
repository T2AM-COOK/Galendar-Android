package com.example.galendar.feature.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.galendar.R
import com.example.galendar.remote.RetrofitBuilder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

class PickerFragment : BottomSheetDialogFragment(R.layout.fragment_picker) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val applyBtn: Button = view.findViewById(R.id.apllyBtn)
        val resetBtn: Button = view.findViewById(R.id.resetBtn)

        var submitStartDate: String = "" // 기본값: 공백
        var submitEndDate: String = "" // 기본값: 공백

        val submitStartData: Button = view.findViewById(R.id.submit_start_data)
        val submitEndData: Button = view.findViewById(R.id.submit_end_data)

        // 날짜 선택 처리 함수
        fun showDatePickerForButton(button: Button) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )
                button.text = formattedDate

                // 선택한 값 저장
                when (button.id) {
                    R.id.submit_start_data -> submitStartDate = formattedDate
                    R.id.submit_end_data -> submitEndDate = formattedDate
                }
            }, year, month, day).show()
        }

        // 버튼 클릭 시 DatePicker 표시
        submitStartData.setOnClickListener {
            showDatePickerForButton(submitStartData)
        }

        submitEndData.setOnClickListener {
            showDatePickerForButton(submitEndData)
        }

        val regionChipGroup: ChipGroup = view.findViewById(R.id.regionchipGroup)
        val targetChipGroup: ChipGroup = view.findViewById(R.id.targetchipGroup)
        var selectedRegionIds: List<Int> = emptyList()
        var selectedTargetIds: List<Int> = emptyList()

        // 지역 ChipGroup 선택 처리
        regionChipGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedRegionIds = mutableListOf()

            for (i in 0 until group.childCount) {
                val chip = group.getChildAt(i) as Chip
                if (chip.isChecked) {
                    selectedRegionIds = selectedRegionIds + (chip.tag as Int)
                }
            }
        }

        // 타겟 ChipGroup 선택 처리
        targetChipGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedTargetIds = mutableListOf()

            for (i in 0 until group.childCount) {
                val chip = group.getChildAt(i) as Chip
                if (chip.isChecked) {
                    selectedTargetIds = selectedTargetIds + (chip.tag as Int)
                    Log.d("item selected", selectedTargetIds.toString())
                }
            }
        }


        applyBtn.setOnClickListener {
            val isRegionSelected = regionChipGroup.checkedChipIds.isNotEmpty()
            val isTargetSelected = targetChipGroup.checkedChipIds.isNotEmpty()
            val isStartDateSelected = submitStartDate.isNotBlank()
            val isEndDateSelected = submitEndDate.isNotBlank()

            // 하나라도 선택되었는지 확인
            if (!(isRegionSelected || isTargetSelected || isStartDateSelected || isEndDateSelected)) {
                Toast.makeText(requireContext(), "적용될게 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val startDateToSend = if (submitStartDate.isNotBlank()) submitStartDate else ""
                val endDateToSend = if (submitEndDate.isNotBlank()) submitEndDate else ""

                try {
                    val contestResponse = RetrofitBuilder.apiService.getContestList(
                        page = 1,
                        size = 10,
                        keyword = "",
                        targets = selectedTargetIds,
                        regions = selectedRegionIds,
                        submitStartDate = startDateToSend,
                        submitEndDate = endDateToSend,
                        bookmarked = false
                    )

                    withContext(Dispatchers.Main) {
                        println(contestResponse) // 서버 응답 확인
                        Toast.makeText(requireContext(), "적용되었습니다.", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                } catch (e: Exception) {
                    // 에러 메시지 출력
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "에러가 발생했습니다: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    e.printStackTrace() // 로그에 에러 출력
                }
            }
        }
        resetBtn.setOnClickListener {
            // 칩 선택 초기화
            for (i in 0 until regionChipGroup.childCount) {
                val chip = regionChipGroup.getChildAt(i) as Chip
                chip.isChecked = false
            }

            for (i in 0 until targetChipGroup.childCount) {
                val chip = targetChipGroup.getChildAt(i) as Chip
                chip.isChecked = false
            }

            // 날짜 선택 초기화
            submitStartData.text = "시작 날짜"
            submitEndData.text = "종료 날짜"
            submitStartDate = ""
            submitEndDate = ""
        }
    }
}


