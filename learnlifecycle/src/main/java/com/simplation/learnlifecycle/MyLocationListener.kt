package com.simplation.learnlifecycle

import android.app.Activity
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Author: Simplation
 * Date: 2020/12/29 19:31
 * Description:
 */
class MyLocationListener(context: Activity, onLocationChangedListener: OnLocationChangedListener) : LifecycleObserver {

    private val TAG = MyLocationListener::class.java.simpleName

    init {
        initLocationManager()
    }

    private fun initLocationManager() {
        // 初始化操作
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startGetLocation() {
        Log.d(TAG, "startGetLocation()")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopGetLocation() {
        Log.d(TAG, "stopGetLocation()")
    }

    interface OnLocationChangedListener {
        fun changed(latitude: Double, longitude: Double)
    }

}