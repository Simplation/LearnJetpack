package com.simplation.mvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.simplation.mvvm.app.network.apiService
import com.simplation.mvvm.app.network.stateCallback.CollectUiState
import com.simplation.mvvm.app.network.stateCallback.ListDataUiState
import com.simplation.mvvm.data.model.bean.CollectResponse
import com.simplation.mvvm.data.model.bean.CollectUrlResponse
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.request

/**
 * Request collect view model
 *
 * @constructor Create empty Request collect view model
 */
open class RequestCollectViewModel : BaseViewModel() {
    private var pageNo = 0

    // 收藏文章
    val collectUiState: MutableLiveData<CollectUiState> = MutableLiveData()

    // 收藏网址
    val collectUrlUiState: MutableLiveData<CollectUiState> = MutableLiveData()

    // 收藏的文章数据
    var ariticleDataState: MutableLiveData<ListDataUiState<CollectResponse>> = MutableLiveData()

    // 收藏的网址数据
    var urlDataState: MutableLiveData<ListDataUiState<CollectUrlResponse>> = MutableLiveData()

    /**
     * 收藏 文章
     * 提醒一下，玩安卓的收藏 和取消收藏 成功后返回的 data 值为 null，所以在 CollectRepository 中的返回值一定要加上 非空？
     * 不然会出错
     */
    fun collect(id: Int) {
        request({ apiService.collect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = true, id = id)
            collectUiState.value = uiState
        }, {
            val uiState = CollectUiState(isSuccess = false, collect = false, errorMsg = it.errMsg, id = id)
            collectUiState.value = uiState
        })
    }

    /**
     * 取消收藏文章
     * 提醒一下，玩安卓的收藏 和取消收藏 成功后返回的 data 值为 null，所以在 CollectRepository 中的返回值一定要加上 非空？
     * 不然会出错
     */
    fun uncollect(id: Int) {
        request({ apiService.unCollect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = false, id = id)
            collectUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = true, errorMsg = it.errMsg, id = id)
            collectUiState.value = uiState
        })
    }

    /**
     * 收藏 文章
     * 提醒一下，玩安卓的收藏 和取消收藏 成功后返回的 data 值为 null，所以在 CollectRepository 中的返回值一定要加上 非空？
     * 不然会出错
     */
    fun collectUrl(name: String, link: String) {
        request({ apiService.collectUrl(name, link) }, {
            val uiState = CollectUiState(isSuccess = true, collect = true, id = it.id)
            collectUrlUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = false, errorMsg = it.errMsg, id = 0)
            collectUrlUiState.value = uiState
        })
    }

    /**
     * 取消收藏网址
     * 提醒一下，玩安卓的收藏 和取消收藏 成功后返回的 data 值为 null，所以在 CollectRepository 中的返回值一定要加上 非空？
     * 不然会出错
     */
    fun uncollectUrl(id: Int) {
        request({ apiService.unCollect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = false, id = id)
            collectUrlUiState.value = uiState
        }, {
            val uiState = CollectUiState(isSuccess = false, collect = true, errorMsg = it.errMsg, id = id)
            collectUrlUiState.value = uiState
        })
    }

    fun getCollectAriticleData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ apiService.getCollectData(pageNo) }, {
            //请求成功
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
            ariticleDataState.value = listDataUiState
        }, {
            // 请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<CollectResponse>()
                )
            ariticleDataState.value = listDataUiState
        })
    }

    fun getCollectUrlData() {
        request({ apiService.getCollectUrlData() }, { it ->
            // 请求成功
            it.map {
                if(it.order==0){
                    it.order = 1
                }
            }
            val listDataUiState =
                ListDataUiState(
                    isRefresh = true,
                    isSuccess = true,
                    hasMore = false,
                    isEmpty = it.isEmpty(),
                    listData = it
                )
            urlDataState.value = listDataUiState
        }, {
            // 请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    listData = arrayListOf<CollectUrlResponse>()
                )
            urlDataState.value = listDataUiState
        })
    }
}
