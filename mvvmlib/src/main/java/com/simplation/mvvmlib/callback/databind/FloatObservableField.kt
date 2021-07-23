package com.simplation.mvvmlib.callback.databind

import androidx.databinding.ObservableField

/**
 * Float observable field
 *      自定义的 Float 类型 ObservableField 提供了默认值，避免取值的时候还要判空
 *
 * @constructor
 *
 * @param value
 */
class FloatObservableField(value: Float = 0F) : ObservableField<Float>(value) {
    override fun get(): Float {
        return super.get()!!
    }
}