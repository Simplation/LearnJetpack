package com.simplation.learnlifecycle

import androidx.lifecycle.LifecycleService

/**
 * Author: Simplation
 * Date: 2021/01/07 20:41
 * Description:
 */
class MyService() : LifecycleService() {

    private var myServiceObserver: MyServiceObserver = MyServiceObserver()

    init {
        lifecycle.addObserver(myServiceObserver)
    }
}