package com.simplation.mvvm.app.network.stateCallback

/**
 * List data ui state
 *
 * @property isSuccess
 * @property errMessage
 * @property isRefresh
 * @property isEmpty
 * @property hasMore
 * @property isFirstEmpty
 * @property listData
 * @constructor
 *
 * @param null
 */
data class ListDataUiState<T>(
    // 是否请求成功
    val isSuccess: Boolean,
    // 错误消息 isSuccess 为 false 才会有
    val errMessage: String = "",
    // 是否为刷新
    val isRefresh: Boolean = false,
    // 是否为空
    val isEmpty: Boolean = false,
    // 是否还有更多
    val hasMore: Boolean = false,
    // 是第一页且没有数据
    val isFirstEmpty: Boolean = false,
    // 列表数据
    val listData: ArrayList<T> = arrayListOf()
)
