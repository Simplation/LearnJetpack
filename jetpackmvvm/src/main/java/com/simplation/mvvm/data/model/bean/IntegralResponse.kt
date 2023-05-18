package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Integral response  积分
 *
 * @property coinCount
 * @property rank
 * @property userId
 * @property username
 * @constructor Create empty Integral response
 */
@Parcelize
data class IntegralResponse(
    var coinCount: Int,// 当前积分
    var rank: Int,
    var userId: Int,
    var username: String
) : Parcelable
