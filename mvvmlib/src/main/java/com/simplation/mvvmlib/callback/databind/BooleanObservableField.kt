package com.simplation.mvvmlib.callback.databind

import androidx.databinding.ObservableField

/**
 * Boolean observable field
 *      自定义的 Boolean 类型 ObservableField 提供了默认值，避免取值的时候还要判空
 *
 * @constructor
 *
 * @param value
 */
class BooleanObservableField(value: Boolean = false) : ObservableField<Boolean>(value) {
    override fun get(): Boolean {
        return super.get()!!
    }
}
