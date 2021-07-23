package com.simplation.mvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.simplation.mvvm.app.network.apiService
import com.simplation.mvvm.app.network.stateCallback.ListDataUiState
import com.simplation.mvvm.data.model.bean.IntegralHistoryResponse
import com.simplation.mvvm.data.model.bean.IntegralResponse
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.request

/**
 * Request integral view model
 *
 * @constructor Create empty Request integral view model
 */
class RequestIntegralViewModel : BaseViewModel() {
    private var pageNo = 1

    // 积分排行数据
    var integralDataState = MutableLiveData<ListDataUiState<IntegralResponse>>()

    // 获取积分历史数据
    var integralHistoryDataState = MutableLiveData<ListDataUiState<IntegralHistoryResponse>>()

    fun getIntegralData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }

        request({ apiService.getIntegralRank(pageNo) }, {
            // 请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            integralDataState.value = listDataUiState
        }, {
            // 请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<IntegralResponse>()
                )
            integralDataState.value = listDataUiState
        })
    }

    fun getIntegralHistoryData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }

        request({ apiService.getIntegralHistory(pageNo)}, {
            // 请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            integralHistoryDataState.value = listDataUiState
        }, {
            // 请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<IntegralHistoryResponse>()
                )
            integralHistoryDataState.value = listDataUiState
        })
    }
}