package com.simplation.learnjetpack

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class PureView(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {

    private val TAG = "MyView"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "onMeasure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // setMeasuredDimension(150, 150)  // 传入的参数会影响 View 的大小，优先级比布局文件中设定的尺寸要高
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.d(TAG, "onLayout")
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        Log.d(TAG, "onDraw")
        super.onDraw(canvas)
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        Log.d(TAG, "layout")
        super.layout(l, t, r, b)
    }

    override fun draw(canvas: Canvas?) {
        Log.d(TAG, "draw")
        super.draw(canvas)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "dispatchTouchEvent: ${MotionEvent.actionToString(event.action)}")
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "onTouchEvent: ${MotionEvent.actionToString(event.action)}")
        return super.onTouchEvent(event)
    }
}