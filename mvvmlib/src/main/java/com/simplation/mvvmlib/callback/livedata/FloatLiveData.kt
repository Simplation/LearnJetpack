package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @作者: Simplation
 * @日期: 2021/4/22 17:54
 * @描述: 自定义的 Float 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 * @更新:
 */

class FloatLiveData(value: Float = 0f) : MutableLiveData<Float>(value) {
    override fun getValue(): Float {
        return super.getValue()!!
    }
}
