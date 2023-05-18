package com.simplation.mvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.simplation.mvvm.app.network.apiService
import com.simplation.mvvm.app.network.stateCallback.ListDataUiState
import com.simplation.mvvm.data.model.bean.ArticleResponse
import com.simplation.mvvm.data.model.bean.ClassifyResponse
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.request
import com.simplation.mvvmlib.state.ResultState

/**
 * Request public number view model
 *
 * @constructor Create empty Request public number view model
 */
class RequestPublicNumberViewModel : BaseViewModel() {
    var pageNo = 1
    var titleData: MutableLiveData<ResultState<ArrayList<ClassifyResponse>>> = MutableLiveData()
    var publicDataState: MutableLiveData<ListDataUiState<ArticleResponse>> = MutableLiveData()

    fun getPublicTitleData() {
        request({ apiService.getPublicTitle() }, titleData)
    }

    fun getPublicData(isRefresh: Boolean, cid: Int) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ apiService.getPublicData(pageNo, cid) }, {
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
            publicDataState.value = listDataUiState
        }, {
            // 请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleResponse>()
                )
            publicDataState.value = listDataUiState
        })
    }
}
