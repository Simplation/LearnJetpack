package com.simplation.mvvmlib.callback.databind

import androidx.databinding.ObservableField

/**
 * @作者: Simplation
 * @日期: 2021/4/23 9:33
 * @描述: 自定义的 Double 类型 ObservableField 提供了默认值，避免取值的时候还要判空
 * @更新:
 */

class DoubleObservableField(value: Double = 0.0) : ObservableField<Double>(value) {
    override fun get(): Double {
        return super.get()!!
    }
}
