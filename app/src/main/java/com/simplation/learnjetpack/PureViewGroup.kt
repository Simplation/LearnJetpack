package com.simplation.learnjetpack

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.RelativeLayout


/**
 * 当我们用手指触摸中间的 PureView 时，系统最先调用的是 PureViewGroup 的 dispatchTouchEvent() 方法，
 * 之后又调用了自身的 onInterceptTouchEvent() 方法。然后紧接着调用了 PureView 的 dispatchTouchEvent()，
 * 接着又是 PureView 的 onTouchEvent() 方法，最后又回到了 PureViewGroup 的 onTouchEvent() 方法。
 * 这就是触摸事件的整体流程，但是很奇怪的是，为什么只有 ACTION_DOWN 事件触发回调呢？
 */
class PureViewGroup(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val TAG = "PureViewGroup"

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG, "dispatchTouchEvent: ${MotionEvent.actionToString(ev.action)}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG, "onInterceptTouchEvent: ${MotionEvent.actionToString(ev.action)}")
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "onTouchEvent: ${MotionEvent.actionToString(event.action)}")
        return super.onTouchEvent(event)
    }
}