package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Collect url response
 *
 * @property icon
 * @property id
 * @property link
 * @property name
 * @property order
 * @property userId
 * @property visible
 * @constructor Create empty Collect url response
 */
@Parcelize
data class CollectUrlResponse(
    var icon: String,
    var id: Int,
    var link: String,
    var name: String,
    var order: Int,
    var userId: Int,
    var visible: Int
) : Parcelable

