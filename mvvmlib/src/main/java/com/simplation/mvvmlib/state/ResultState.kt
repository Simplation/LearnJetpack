package com.simplation.mvvmlib.state

import androidx.lifecycle.MutableLiveData
import com.simplation.mvvmlib.network.AppException
import com.simplation.mvvmlib.network.BaseResponse
import com.simplation.mvvmlib.network.ExceptionHandle

/**
 * Result state
 *
 * @param T
 * @constructor Create empty Result state
 */
sealed class ResultState<out T> {
    companion object {
        fun <T> onAppSuccess(data: T): ResultState<T> = Success(data)
        fun <T> onAppLoading(loadingMessage: String): ResultState<T> = Loading(loadingMessage)
        fun <T> onAppError(error: AppException): ResultState<T> = Error(error)
    }

    data class Success<out T>(val data: T) : ResultState<T>()
    data class Loading(val loadingMessage: String) : ResultState<Nothing>()
    data class Error(val error: AppException) : ResultState<Nothing>()
}

/**
 * Pares result：处理返回值
 *
 * @param T
 * @param result
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(result: BaseResponse<T>) {
    value = when {
        result.isSuccess() -> {
            ResultState.onAppSuccess(result.getResponseData())
        }
        else -> {
            ResultState.onAppError(AppException(result.getResponseCode(), result.getResponseMsg()))
        }
    }
}

/**
 * Pares result：不处理返回值 直接返回请求结果
 *
 * @param T
 * @param result
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(result: T) {
    value = ResultState.onAppSuccess(result)
}

/**
 * Pares exception：异常转换异常处理
 *
 * @param T
 * @param e
 */
fun <T> MutableLiveData<ResultState<T>>.paresException(e: Throwable) {
    this.value = ResultState.onAppError(ExceptionHandle.handleException(e))
}
