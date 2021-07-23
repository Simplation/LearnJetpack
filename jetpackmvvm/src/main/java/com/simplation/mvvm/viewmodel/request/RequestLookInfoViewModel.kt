package com.simplation.mvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.simplation.mvvm.app.network.apiService
import com.simplation.mvvm.app.network.stateCallback.ListDataUiState
import com.simplation.mvvm.data.model.bean.ArticleResponse
import com.simplation.mvvm.data.model.bean.ShareResponse
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.request

/**
 * Request look info view model
 *
 * @constructor Create empty Request look info view model
 */
class RequestLookInfoViewModel : BaseViewModel() {
    var pageNo = 1

    var shareListDataUiState = MutableLiveData<ListDataUiState<ArticleResponse>>()

    var shareResponse = MutableLiveData<ShareResponse>()

    fun getLookInfo(id: Int, isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ apiService.getShareByIdData(id, pageNo) }, {
            // 请求成功
            pageNo++
            shareResponse.value = it
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = it.shareArticles.isRefresh(),
                    isEmpty = it.shareArticles.isEmpty(),
                    hasMore = it.shareArticles.hasMore(),
                    isFirstEmpty = isRefresh && it.shareArticles.isEmpty(),
                    listData = it.shareArticles.datas
                )
            shareListDataUiState.value = listDataUiState
        }, {
            // 请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleResponse>()
                )
            shareListDataUiState.value = listDataUiState
        })
    }
}