package com.simplation.mvvm.app.network.stateCallback

/**
 * Collect ui state
 *
 * @property isSuccess
 * @property collect
 * @property id
 * @property errorMsg
 * @constructor Create empty Collect ui state
 */
data class CollectUiState(
    // 请求是否成功
    var isSuccess: Boolean = true,
    // 收藏
    var collect: Boolean = false,
    // 收藏 Id
    var id: Int = -1,
    // 请求失败的错误信息
    var errorMsg: String = ""
)
