package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Share response
 *
 * @property coinInfo
 * @property shareArticles
 * @constructor Create empty Share response
 */
@Parcelize
data class ShareResponse(
    var coinInfo: CoinInfoResponse,
    var shareArticles: ApiPagerResponse<ArrayList<ArticleResponse>>
) : Parcelable