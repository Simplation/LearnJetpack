package com.simplation.mvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.simplation.mvvm.app.network.apiService
import com.simplation.mvvm.data.model.bean.IntegralResponse
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.request
import com.simplation.mvvmlib.state.ResultState

/**
 * @作者: Simplation
 * @日期: 2021/4/28 11:51
 * @描述:
 * @更新:
 */

class RequestMeViewModel : BaseViewModel() {
    val meData = MutableLiveData<ResultState<IntegralResponse>>()

    fun getIntegral() {
        request({ apiService.getIntegral() }, meData)
    }
}
