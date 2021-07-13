package com.simplation.mvvm.app.network

import com.simplation.mvvm.app.util.CacheUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * My head interceptor
 *      自定义头部参数拦截器，传入 heads
 *
 * @constructor Create empty My head interceptor
 */
class MyHeadInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("token", "token123456").build()
        builder.addHeader("device", "Android").build()
        builder.addHeader("isLogin", CacheUtil.isLogin().toString()).build()
        return chain.proceed(builder.build())
    }
}