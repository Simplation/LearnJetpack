package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Int live data
 *      自定义的 Int 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 * @constructor Create empty Int live data
 */
class IntLiveData : MutableLiveData<Int>() {
    override fun getValue(): Int {
        return super.getValue() ?: 0
    }
}