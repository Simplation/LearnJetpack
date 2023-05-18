package com.simplation.mvvmlib.ext.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.simplation.mvvmlib.callback.livedata.BooleanLiveData

/**
 * Ktx app life observer
 *
 * @constructor Create empty Ktx app life observer
 */
object KtxAppLifeObserver : LifecycleObserver {
    private val isForeground = BooleanLiveData()

    // 在前台
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onForeground() {
        isForeground.value = true
    }

    // 在后台
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onBackground() {
        isForeground.value = false
    }
}