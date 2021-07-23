package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Short live data
 *      自定义的 Short 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 * @constructor Create empty Short live data
 */
class ShortLiveData : MutableLiveData<Short>() {
    override fun getValue(): Short {
        return super.getValue() ?: 0
    }
}