package com.example.galendar.util

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.CalendarDay

class DotDecorator(
    private val context: Context,
    internal val dates: Set<CalendarDay>,
    private val selectedDate: CalendarDay? = null  // 선택된 날짜 추가
) : DayViewDecorator {

    private val dotColor = Color.parseColor("#2B32B2")
    private val dotRadius = 6f

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(CustomDotSpan(dotRadius, dotColor))
        // 선택된 날짜가 아닐 때만 검은색 텍스트 적용
        if (selectedDate == null || !dates.contains(selectedDate)) {
            view.addSpan(ForegroundColorSpan(Color.BLACK))
        }
    }
}