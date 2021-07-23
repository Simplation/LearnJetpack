package com.simplation.mvvmlib.callback.databind

import androidx.databinding.ObservableField

/**
 * Int observable field
 *      自定义的 Int 类型 ObservableField 提供了默认值，避免取值的时候还要判空
 *
 * @constructor
 *
 * @param value
 */
class IntObservableField(value: Int = 0) : ObservableField<Int>(value) {
    override fun get(): Int {
        return super.get()!!
    }
}