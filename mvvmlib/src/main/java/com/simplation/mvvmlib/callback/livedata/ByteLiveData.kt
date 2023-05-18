package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Byte live data
 *      自定义的 Byte 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 * @constructor Create empty Byte live data
 */
class ByteLiveData : MutableLiveData<Byte>() {
    override fun getValue(): Byte {
        return super.getValue() ?: 0
    }
}
