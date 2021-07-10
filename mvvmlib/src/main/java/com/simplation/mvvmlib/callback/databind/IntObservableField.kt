package com.simplation.mvvmlib.callback.databind

import androidx.databinding.ObservableField

/**
 * @作者: Simplation
 * @日期: 2021/4/23 9:36
 * @描述: 自定义的 Int 类型 ObservableField 提供了默认值，避免取值的时候还要判空
 * @更新:
 */

class IntObservableField(value: Int = 0) : ObservableField<Int>(value) {
    override fun get(): Int {
        return super.get()!!
    }
}