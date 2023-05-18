package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Boolean live data
 *      自定义的 Boolean 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 * @constructor Create empty Boolean live data
 */
class BooleanLiveData : MutableLiveData<Boolean>() {
    override fun getValue(): Boolean {
        return super.getValue() ?: false
    }
}