package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Double live data
 *      自定义的 Double 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 * @constructor Create empty Double live data
 */
class DoubleLiveData : MutableLiveData<Double>() {

    override fun getValue(): Double {
        return super.getValue() ?: 0.0
    }
}