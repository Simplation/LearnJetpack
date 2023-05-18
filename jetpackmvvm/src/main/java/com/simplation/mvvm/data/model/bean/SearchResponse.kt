package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Search response 搜索热词
 *
 * @property id
 * @property link
 * @property name
 * @property order
 * @property visible
 * @constructor Create empty Search response
 */
@Parcelize
data class SearchResponse(
    var id: Int,
    var link: String,
    var name: String,
    var order: Int,
    var visible: Int
) : Parcelable