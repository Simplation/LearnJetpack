package com.simplation.mvvm.app.weight.loadCallback

import com.kingja.loadsir.callback.Callback
import com.simplation.mvvm.R

/**
 * Empty callback
 *
 * @constructor Create empty Empty callback
 */
class EmptyCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }
}