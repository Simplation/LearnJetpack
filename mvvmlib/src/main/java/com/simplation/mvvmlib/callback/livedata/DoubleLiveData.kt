package com.sunnyit.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @作者: Simplation
 * @日期: 2021/4/22 17:53
 * @描述: 自定义的 Double 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 * @更新:
 */
class DoubleLiveData : MutableLiveData<Double>() {

    override fun getValue(): Double {
        return super.getValue() ?: 0.0
    }
}