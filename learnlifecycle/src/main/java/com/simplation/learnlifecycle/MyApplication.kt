package com.simplation.learnlifecycle

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * Author: Simplation
 * Date: 2021/01/07 21:13
 * Description:
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationObserver())
    }
}