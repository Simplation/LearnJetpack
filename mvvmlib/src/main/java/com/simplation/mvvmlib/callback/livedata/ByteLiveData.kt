package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @作者: Simplation
 * @日期: 2021/4/22 17:51
 * @描述: 自定义的 Byte 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 * @更新:
 */
class ByteLiveData : MutableLiveData<Byte>() {
    override fun getValue(): Byte {
        return super.getValue() ?: 0
    }
}
