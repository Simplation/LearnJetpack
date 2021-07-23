package com.simplation.mvvmlib.callback.databind

import androidx.databinding.ObservableField

/**
 * String observable field
 *      自定义的 String 类型 ObservableField 提供了默认值，避免取值的时候还要判空
 *
 * @constructor
 *
 * @param value
 */
class StringObservableField(value: String = "") : ObservableField<String>(value) {
    override fun get(): String {
        return super.get()!!
    }
}