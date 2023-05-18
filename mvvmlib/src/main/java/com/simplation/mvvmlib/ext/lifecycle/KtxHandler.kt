package com.simplation.mvvmlib.ext.lifecycle

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * Ktx handler
 *
 * @constructor
 *
 * @param lifecycleOwner
 * @param callback
 */
class KtxHandler(lifecycleOwner: LifecycleOwner, callback: Callback) : Handler(callback),
    LifecycleObserver {

    private val mLifecycleOwner: LifecycleOwner = lifecycleOwner

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        removeCallbacksAndMessages(null)
        mLifecycleOwner.lifecycle.removeObserver(this)
    }
}
