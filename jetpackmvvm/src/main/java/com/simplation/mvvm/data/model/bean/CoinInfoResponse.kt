package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Coin info response
 *
 * @property coinCount
 * @property rank
 * @property userId
 * @property username
 * @constructor Create empty Coin info response
 */
@Parcelize
data class CoinInfoResponse(
    var coinCount: Int,
    var rank: String,
    var userId: Int,
    var username: String
) : Parcelable