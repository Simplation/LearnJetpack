package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Banner response
 *
 * @property desc
 * @property id
 * @property imagePath
 * @property isVisible
 * @property order
 * @property title
 * @property type
 * @property url
 * @constructor Create empty Banner response
 */
@Parcelize
data class BannerResponse(
    var desc: String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
) : Parcelable


