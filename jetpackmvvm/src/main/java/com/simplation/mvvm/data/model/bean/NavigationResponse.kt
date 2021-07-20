package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Navigation response
 *
 * @property articles
 * @property cid
 * @property name
 * @constructor Create empty Navigation response
 */
@Parcelize
data class NavigationResponse(
    var articles: ArrayList<ArticleResponse>,
    var cid: Int,
    var name: String
) : Parcelable
