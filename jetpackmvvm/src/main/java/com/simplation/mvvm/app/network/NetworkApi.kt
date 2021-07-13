package com.simplation.mvvm.app.network

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import com.simplation.mvvmlib.base.appContext
import com.simplation.mvvmlib.network.BaseNetworkApi
import com.simplation.mvvmlib.network.interceptor.CacheInterceptor
import com.simplation.mvvmlib.network.interceptor.logging.LogInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


val apiService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.SERVER_URL)
}

class NetworkApi : BaseNetworkApi() {
    companion object {
        val INSTANCE: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApi()
        }
    }

    private val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(appContext))
    }

    /**
     * Set http client builder
     *      实现重写父类的 setHttpClientBuilder 方法，
     *      这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     *
     * @param builder
     * @return
     */
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            // 设置缓存配置，最大为 10M
            cache(Cache(File(appContext.filesDir, "mvvm_cache"), (10 * 10 * 1024).toLong()))
            // 添加 Cookies 自动持久化
            cookieJar(cookieJar)
            // 添加拦截器
            addInterceptor(MyHeadInterceptor())
            addInterceptor(CacheInterceptor()) // 缓存拦截器，可传入缓存天数，不传默认 7 天
            addInterceptor(LogInterceptor())   // 日志拦截器
            // 设置超时时间
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
        }

        return builder
    }

    /**
     * Set retrofit builder
     *      实现重写父类的 setRetrofitBuilder 方法，
     *      在这里可以对 Retrofit.Builder 做任意操作，比如添加 GSON 解析器，protobuf 等
     *
     * @param builder
     * @return
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        }
    }
}

