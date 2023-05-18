package com.simplation.mvvmlib.callback.databind

import androidx.databinding.ObservableField

/**
 * Double observable field
 *      自定义的 Double 类型 ObservableField 提供了默认值，避免取值的时候还要判空
 *
 * @constructor
 *
 * @param value
 */
class DoubleObservableField(value: Double = 0.0) : ObservableField<Double>(value) {
    override fun get(): Double {
        return super.get()!!
    }
}
