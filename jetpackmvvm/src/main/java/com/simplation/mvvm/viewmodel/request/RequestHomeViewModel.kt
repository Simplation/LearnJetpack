package com.simplation.mvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.simplation.mvvm.app.network.apiService
import com.simplation.mvvm.app.network.stateCallback.ListDataUiState
import com.simplation.mvvm.data.model.bean.ArticleResponse
import com.simplation.mvvm.data.model.bean.BannerResponse
import com.simplation.mvvm.data.repository.request.HttpRequestCoroutine
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.request
import com.simplation.mvvmlib.state.ResultState

/**
 * @作者: Simplation
 * @日期: 2021/4/27 10:46
 * @描述: 有两种回调方式：
 * 1.首页文章列表 将返回的数据放在 ViewModel 中过滤包装给 activity/fragment 去使用
 * 2.首页轮播图 将返回的数据直接给 activity/fragment 去处理使用
 * 可以根据个人理解与喜好使用(建议 简单的不需要做数据过滤包装的能直接用返回数据的可以直接用 2   复杂的需要自己封装一下让使用变的更方便的可以使用1  )
 * @更新:
 */

class RequestHomeViewModel : BaseViewModel() {

    // 页码 首页数据页码从0开始
    var pageNo = 0

    // 首页文章列表数据
    var homeDataState: MutableLiveData<ListDataUiState<ArticleResponse>> = MutableLiveData()

    // 首页轮播图数据
    var bannerData: MutableLiveData<ResultState<ArrayList<BannerResponse>>> = MutableLiveData()

    /**
     * 获取首页文章列表数据
     * @param isRefresh 是否是刷新，即第一页
     */
    fun getHomeData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }

        request({ HttpRequestCoroutine.getHomeData(pageNo) }, {
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
            homeDataState.value = listDataUiState
        }, {
            // 请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleResponse>()
                )
            homeDataState.value = listDataUiState
        })
    }

    /**
     * 获取轮播图数据
     */
    fun getBannerData() {
        request({ apiService.getBanner() }, bannerData)
    }
}