package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @作者: Simplation
 * @日期: 2021/4/22 17:55
 * @描述: 自定义的 Int 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 * @更新:
 */

class IntLiveData : MutableLiveData<Int>() {
    override fun getValue(): Int {
        return super.getValue() ?: 0
    }
}