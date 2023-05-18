package com.simplation.mvvm.app.ext

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter


var adapterLastClickTime = 0L

fun BaseQuickAdapter<*, *>.setNbOnItemClickListener(
    interval: Long = 1000,
    action: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit
) {
    setOnItemClickListener { adapter, view, position ->
        val currentTime = System.currentTimeMillis()
        if (adapterLastClickTime != 0L && (currentTime - adapterLastClickTime < interval)) {
            return@setOnItemClickListener
        }
        adapterLastClickTime = currentTime
        action(adapter, view, position)
    }
}

var adapterChildLastClickTime = 0L

fun BaseQuickAdapter<*, *>.setNbOnItemChildClickListener(
    interval: Long = 1000,
    action: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit
) {
    setOnItemChildClickListener { adapter, view, position ->
        val currentTime = System.currentTimeMillis()
        if (adapterChildLastClickTime != 0L && (currentTime - adapterChildLastClickTime < interval)) {
            return@setOnItemChildClickListener
        }
        adapterChildLastClickTime = currentTime
        action(adapter, view, position)
    }
}
