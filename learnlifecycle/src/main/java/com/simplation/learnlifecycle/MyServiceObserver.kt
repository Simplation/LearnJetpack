package com.simplation.learnlifecycle

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Author: Simplation
 * Date: 2021/01/07 20:53
 * Description:
 */
class MyServiceObserver : LifecycleObserver {
    private val TAG = this.javaClass.simpleName

    // 当 Service 的 onStart 的时候，该方法会被调用
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun startGetLocation() {
        Log.d(TAG, "startGetLocation()")
    }

    // 当 Service 的 onDestroy 的时候，该方法会被调用
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stopGetLocation() {
        Log.d(TAG, "stopGetLocation()")
    }
}