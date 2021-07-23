package com.simplation.mvvm.viewmodel.state

import androidx.databinding.ObservableField
import com.simplation.mvvm.data.model.bean.IntegralResponse
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel

/**
 * Integral view model
 *
 * @constructor Create empty Integral view model
 */
class IntegralViewModel : BaseViewModel() {
    var rank = ObservableField<IntegralResponse>()
}