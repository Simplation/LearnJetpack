package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * String live data
 *      自定义的 String 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 * @constructor Create empty String live data
 */
class StringLiveData : MutableLiveData<String>() {
    override fun getValue(): String {
        return super.getValue() ?: ""
    }
}
