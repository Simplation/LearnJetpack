package com.simplation.mvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.simplation.mvvm.app.network.apiService
import com.simplation.mvvm.app.network.stateCallback.ListDataUiState
import com.simplation.mvvm.data.model.bean.ArticleResponse
import com.simplation.mvvm.data.model.bean.NavigationResponse
import com.simplation.mvvm.data.model.bean.SystemResponse
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.request

/**
 * Request tree view model
 *
 * @constructor Create empty Request tree view model
 */
class RequestTreeViewModel : BaseViewModel() {

    // 页码，体系 广场的页码是从 0 开始的
    private var pageNo = 0

    // 广场数据
    var plazaDataState: MutableLiveData<ListDataUiState<ArticleResponse>> = MutableLiveData()

    // 每日一问数据
    var askDataState: MutableLiveData<ListDataUiState<ArticleResponse>> = MutableLiveData()

    // 体系子栏目列表数据
    var systemChildDataState: MutableLiveData<ListDataUiState<ArticleResponse>> = MutableLiveData()

    // 体系数据
    var systemDataState: MutableLiveData<ListDataUiState<SystemResponse>> = MutableLiveData()

    // 导航数据
    var navigationDataState: MutableLiveData<ListDataUiState<NavigationResponse>> =
        MutableLiveData()

    /**
     * 获取广场数据
     */
    fun getPlazaData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ apiService.getSquareData(pageNo) }, {
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
            plazaDataState.value = listDataUiState
        }, {
            // 请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleResponse>()
                )
            plazaDataState.value = listDataUiState
        })
    }

    /**
     * 获取每日一问数据
     */
    fun getAskData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1 // 每日一问的页码从 1 开始
        }
        request({ apiService.getAskData(pageNo) }, {
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
            askDataState.value = listDataUiState
        }, {
            // 请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleResponse>()
                )
            askDataState.value = listDataUiState
        })
    }

    /**
     * 获取体系数据
     */
    fun getSystemData() {
        request({ apiService.getSystemData() }, {
            // 请求成功
            val dataUiState =
                ListDataUiState(
                    isSuccess = true,
                    listData = it
                )
            systemDataState.value = dataUiState
        }, {
            // 请求失败
            val dataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    listData = arrayListOf<SystemResponse>()
                )
            systemDataState.value = dataUiState
        })
    }

    /**
     * 获取导航数据
     */
    fun getNavigationData() {
        request({ apiService.getNavigationData() }, {
            // 请求成功
            val dataUiState =
                ListDataUiState(
                    isSuccess = true,
                    listData = it
                )
            navigationDataState.value = dataUiState
        }, {
            // 请求失败
            val dataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    listData = arrayListOf<NavigationResponse>()
                )
            navigationDataState.value = dataUiState
        })
    }

    /**
     * 获取体系子栏目列表数据
     */
    fun getSystemChildData(isRefresh: Boolean, cid: Int) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ apiService.getSystemChildData(pageNo, cid) }, {
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
            systemChildDataState.value = listDataUiState
        }, {
            // 请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleResponse>()
                )
            plazaDataState.value = listDataUiState
        })
    }

}

