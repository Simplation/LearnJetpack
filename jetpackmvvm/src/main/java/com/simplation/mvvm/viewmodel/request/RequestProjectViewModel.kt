package com.simplation.mvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.simplation.mvvm.app.network.apiService
import com.simplation.mvvm.app.network.stateCallback.ListDataUiState
import com.simplation.mvvm.data.model.bean.ArticleResponse
import com.simplation.mvvm.data.model.bean.ClassifyResponse
import com.simplation.mvvm.data.repository.request.HttpRequestCoroutine
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.request
import com.simplation.mvvmlib.state.ResultState

/**
 * Request project view model
 *
 * @constructor Create empty Request project view model
 */
class RequestProjectViewModel : BaseViewModel() {
    // 页码
    var pageNo = 1

    var titleData: MutableLiveData<ResultState<ArrayList<ClassifyResponse>>> = MutableLiveData()

    var projectDataState: MutableLiveData<ListDataUiState<ArticleResponse>> = MutableLiveData()

    fun getProjectTitleData() {
        request({ apiService.getProjectTitle() }, titleData)
    }

    fun getProjectData(isRefresh: Boolean, cid: Int, isNew: Boolean = false) {
        if (isRefresh) {
            pageNo = if (isNew) 0 else 1
        }

        request({ HttpRequestCoroutine.getProjectData(pageNo, cid, isNew) }, {
            // 請求成功
            pageNo++
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = isRefresh,
                isEmpty = it.isEmpty(),
                hasMore = it.hasMore(),
                isFirstEmpty = isRefresh && it.isEmpty(),
                listData = it.datas
            )
            projectDataState.value = listDataUiState
        }, {
            // 請求失敗
            val listDataUiState = ListDataUiState(
                isSuccess = false,
                errMessage = it.errMsg,
                isRefresh = isRefresh,
                listData = arrayListOf<ArticleResponse>()
            )
            projectDataState.value = listDataUiState
        })
    }
}
