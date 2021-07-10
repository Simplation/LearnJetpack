package com.simplation.mvvmlib.callback.databind

import androidx.databinding.ObservableField

/**
 * @作者: Simplation
 * @日期: 2021/4/23 9:32
 * @描述: 自定义的 Byte 类型 ObservableField 提供了默认值，避免取值的时候还要判空
 * @更新:
 */

class ByteObservableField(value: Byte = 0) : ObservableField<Byte>(value) {
    override fun get(): Byte {
        return super.get()!!
    }
}