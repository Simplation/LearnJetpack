package com.simplation.mvvmlib.callback.databind

import androidx.databinding.ObservableField

/**
 * Byte observable field
 *      自定义的 Byte 类型 ObservableField 提供了默认值，避免取值的时候还要判空
 *
 * @constructor
 *
 * @param value
 */
class ByteObservableField(value: Byte = 0) : ObservableField<Byte>(value) {
    override fun get(): Byte {
        return super.get()!!
    }
}