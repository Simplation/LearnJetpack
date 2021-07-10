package com.simplation.mvvmlib.callback.databind

import androidx.databinding.ObservableField

/**
 * @作者: Simplation
 * @日期: 2021/4/22 18:06
 * @描述: 自定义的 Boolean 类型 ObservableField 提供了默认值，避免取值的时候还要判空
 * @更新:
 */


class BooleanObservableField(value: Boolean = false) : ObservableField<Boolean>(value) {
    override fun get(): Boolean {
        return super.get()!!
    }
}
