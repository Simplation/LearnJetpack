package com.simplation.mvvm.data.repository.request

import com.simplation.mvvm.app.network.apiService
import com.simplation.mvvm.app.util.CacheUtil
import com.simplation.mvvm.data.model.bean.ApiPagerResponse
import com.simplation.mvvm.data.model.bean.ApiResponse
import com.simplation.mvvm.data.model.bean.ArticleResponse
import com.simplation.mvvm.data.model.bean.UserInfo
import com.simplation.mvvmlib.network.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Http request coroutine
 * 处理协程的请求类
 */

val HttpRequestCoroutine: HttpRequestManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    HttpRequestManager()
}

class HttpRequestManager {
    suspend fun getHomeData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>> {
        return withContext(Dispatchers.IO) {
            val listData = async { apiService.getArticleList(pageNo) }
            // 如果 App 配置打开了首页请求置顶文章，且是第一页
            if (CacheUtil.isNeedTop() && pageNo == 0) {
                val topData = async { apiService.getTopArticleList() }
                listData.await().data.datas.addAll(0, topData.await().data)
                listData.await()
            } else {
                listData.await()
            }
        }
    }

    suspend fun register(username: String, password: String): ApiResponse<UserInfo> {
        val registerData = apiService.register(username, password, password)
        // 判断注册结果 注册成功，调用登录接口
        if (registerData.isSuccess()) {
            return apiService.login(username, password)
        } else {
            throw AppException(registerData.errorCode, registerData.errorMessage)
        }
    }

    suspend fun getProjectData(
        pageNo: Int,
        cid: Int,
        isNew: Boolean
    ): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>> {
        return if (isNew) {
            apiService.getProjecNewData(pageNo)
        } else {
            apiService.getProjectDataByType(pageNo, cid)
        }
    }

}
