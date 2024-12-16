package com.example.galendar.util

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.CalendarDay

class SelectedWhiteDecorator : DayViewDecorator {
    private var selectedDate: CalendarDay? = null

    fun setSelectedDay(date: CalendarDay) {
        this.selectedDate = date
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return selectedDate != null && day == selectedDate
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.WHITE))  // 선택된 날짜는 흰색 텍스트
    }
}