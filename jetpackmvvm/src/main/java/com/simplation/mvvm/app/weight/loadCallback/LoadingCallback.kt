package com.simplation.mvvm.app.weight.loadCallback

import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import com.simplation.mvvm.R

/**
 * Loading callback
 *
 * @constructor Create empty Loading callback
 */
class LoadingCallback : Callback() {
    override fun onCreateView(): Int = R.layout.layout_loading

    override fun onReloadEvent(context: Context?, view: View?): Boolean = true
}