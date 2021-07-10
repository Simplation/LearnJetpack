package com.simplation.mvvmlib.network.interceptor

import com.simplation.mvvmlib.base.appContext
import com.simplation.mvvmlib.network.NetworkUtil
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @作者: Simplation
 * @日期: 2021/4/20 17:44
 * @描述: 缓存拦截器，默认是 7 天
 * @更新:
 */

class CacheInterceptor(var day: Int = 7) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetworkUtil.isNetworkAvailable(appContext)) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }
        val response = chain.proceed(request)
        if (!NetworkUtil.isNetworkAvailable(appContext)) {
            val maxAge = 60 * 60
            response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=$maxAge")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * day // tolerate 4-weeks stale
            response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
        return response
    }

}