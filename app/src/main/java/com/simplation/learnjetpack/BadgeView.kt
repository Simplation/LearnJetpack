package com.simplation.learnjetpack

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout.LayoutParams

import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout

class BadgeView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    init {
        init()
    }

    private fun init() {
        if (layoutParams !is ConstraintLayout.LayoutParams) {
            val layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT
            )
            setLayoutParams(layoutParams)
        }
        setTextColor(Color.WHITE)
        typeface = Typeface.DEFAULT
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F)
        setPadding(5, 1, 5, 1)
        setBackgroundColor(Color.RED)
        gravity = Gravity.CENTER

    }
}