package com.example.galendar

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.Calendar
import android.util.Log

class WeekendDecorator : DayViewDecorator {
    private val color: Int = Color.parseColor("#2B32B2") // 원하는 색상

    override fun shouldDecorate(day: CalendarDay): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(day.year, day.month - 1, day.day)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        Log.d(
            "WeekendDecorator",
            "Day: ${day.day}, Month: ${day.month}, Year: ${day.year}, Day of Week: $dayOfWeek"
        )
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.parseColor("#2B32B2"))) // 요일 색상 설정
    }
}
