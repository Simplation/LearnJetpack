package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Integral history response  积分记录
 *
 * @property coinCount
 * @property date
 * @property desc
 * @property id
 * @property type
 * @property reason
 * @property userId
 * @property userName
 * @constructor Create empty Integral history response
 */
@Parcelize
data class IntegralHistoryResponse(
    var coinCount: Int,
    var date: Long,
    var desc: String,
    var id: Int,
    var type: Int,
    var reason: String,
    var userId: Int,
    var userName: String) : Parcelable