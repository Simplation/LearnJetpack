package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Float live data
 *      自定义的 Float 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 * @constructor
 *
 * @param value
 */
class FloatLiveData(value: Float = 0f) : MutableLiveData<Float>(value) {
    override fun getValue(): Float {
        return super.getValue()!!
    }
}
