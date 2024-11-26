package com.example.galendar.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.Calendar
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Canvas


class SundayDecorator : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(day.year, day.month - 1, day.day) // Calendar의 month는 0부터 시작합니다
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.parseColor("#2B32B2")))
    }
}

class SaturdayDecorator : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(day.year, day.month - 1, day.day) // Calendar의 month는 0부터 시작합니다
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.parseColor("#2B32B2")))
    }
}
class SelectedDecorator : DayViewDecorator {

    private var selectedDay: CalendarDay? = null

    fun setSelectedDay(day: CalendarDay) {
        selectedDay = day
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return selectedDay == day
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.WHITE)) // 선택된 날짜는 흰색으로 표시
    }
}

