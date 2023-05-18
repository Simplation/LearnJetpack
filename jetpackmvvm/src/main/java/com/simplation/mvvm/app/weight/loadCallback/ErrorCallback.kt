package com.simplation.mvvm.app.weight.loadCallback

import com.kingja.loadsir.callback.Callback
import com.simplation.mvvm.R

/**
 * Error callback
 *
 * @constructor Create empty Error callback
 */
class ErrorCallback:Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_error
    }
}