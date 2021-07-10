package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @作者: Simplation
 * @日期: 2021/4/20 16:38
 * @描述: 自定义的 Boolean 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 * @更新:
 */

class BooleanLiveData : MutableLiveData<Boolean>() {
    override fun getValue(): Boolean {
        return super.getValue() ?: false
    }
}