package com.example.galendar.util

import android.graphics.Canvas
import android.graphics.Paint
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class CustomDotSpan(
    private val radius: Float = 3f,
    private val color: Int
) : DotSpan(radius, color) {

    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        lnum: Int
    ) {
        val cx = ((left + right) / 2).toFloat()
        val cy = bottom +5f
        paint.color = color
        canvas.drawCircle(cx, cy, radius, paint)
    }
}