package com.simplation.mvvm.data.model.bean

import java.io.Serializable

/**
 * Api pager response
 *
 * @param T
 * @property datas
 * @property curPage
 * @property offset
 * @property over
 * @property pageCount
 * @property size
 * @property total
 * @constructor Create empty Api pager response
 */
data class ApiPagerResponse<T>(
    var datas: T,
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
) : Serializable {
    /**
     * 数据是否为空
     */
    fun isEmpty() = (datas as List<*>).size == 0

    /**
     * 是否为刷新
     */
    fun isRefresh() = offset == 0

    /**
     * 是否还有更多数据
     */
    fun hasMore() = !over
}