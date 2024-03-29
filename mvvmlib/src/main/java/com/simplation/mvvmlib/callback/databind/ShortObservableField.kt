package com.simplation.mvvmlib.callback.databind

import androidx.databinding.ObservableField

/**
 * Short observable field
 *      自定义的 Short 类型 ObservableField 提供了默认值，避免取值的时候还要判空
 *
 * @constructor
 *
 * @param value
 */
class ShortObservableField(value: Short = 0):ObservableField<Short>(value) {
    override fun get(): Short {
        return super.get()!!
    }
}