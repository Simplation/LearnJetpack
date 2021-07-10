package com.simplation.learnviewmodel

import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.properties.Delegates

/**
 * Author: Simplation
 * Date: 2021/01/25 22:14
 * Description:
 */
public class TimerViewModel : ViewModel() {

    private var timer: Timer? = null
    private var currentSecond by Delegates.notNull<Int>()

    fun startTiming() {
        if (timer == null) {
            currentSecond = 0
            timer = Timer()
            val timerTask = object : TimerTask() {
                override fun run() {
                    currentSecond++
                    onTimeChangeListener.onTimeChanged(currentSecond)
                }
            }
            timer!!.schedule(timerTask, 1000, 1000)
        }
    }

    interface OnTimeChangeListener {
        fun onTimeChanged(second: Int)
    }

    private lateinit var onTimeChangeListener: OnTimeChangeListener

    fun setOnTimeChangeListener(onTimeChangeListener: OnTimeChangeListener) {
        this.onTimeChangeListener = onTimeChangeListener
    }

    override fun onCleared() {
        super.onCleared()
        // 当 ViewModel 不再被需要，即与之相关的 Activity 都被销毁时，该方法会被系统调用。
        // 释放资源操作

        timer!!.cancel()
    }
}