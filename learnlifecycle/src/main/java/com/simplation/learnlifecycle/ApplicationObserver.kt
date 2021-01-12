package com.simplation.learnlifecycle

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Author: Simplation
 * Date: 2021/01/07 21:14
 * Description:
 */
class ApplicationObserver : LifecycleObserver {
    private val TAG = this.javaClass.simpleName

    // 在应用程序的生命周期中只会被调用一次
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.d(TAG, "onCreate: Lifecycle.Event.ON_CREATE")
    }

    // 在应用程序在前台时出现被调用
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.d(TAG, "onStart: Lifecycle.Event.ON_START")
    }

    // 当应用程序在前台出现的时候被调用
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.d(TAG, "onResume: Lifecycle.Event.ON_RESUME")
    }

    // 当应用程序退出后台时被调用
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.d(TAG, "onPause: Lifecycle.Event.ON_PAUSE")
    }

    // 当应用程序退出后台时被调用
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Log.d(TAG, "onStop: Lifecycle.Event.ON_STOP")
    }

    // 永远不会被调用，系统不会分发调用 ON_DESTROY 方法
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.d(TAG, "onDestroy: Lifecycle.Event.ON_DESTROY")
    }
}