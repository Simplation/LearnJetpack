package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @作者: Simplation
 * @日期: 2021/4/22 17:56
 * @描述: 自定义的 Short 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 * @更新:
 */

class ShortLiveData : MutableLiveData<Short>() {
    override fun getValue(): Short {
        return super.getValue() ?: 0
    }
}