package com.simplation.mvvm.app.network.stateCallback

/**
 * Update ui state
 *
 * @param T
 * @property isSuccess
 * @property data
 * @property errorMsg
 * @constructor Create empty Update ui state
 */
data class UpdateUiState<T>(
    // 请求是否成功
    var isSuccess: Boolean = true,
    // 操作的对象
    var data: T? = null,
    // 请求失败的错误信息
    var errorMsg: String = ""
)
