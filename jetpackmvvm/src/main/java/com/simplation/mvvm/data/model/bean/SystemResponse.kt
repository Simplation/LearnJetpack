package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * System response
 *
 * @property children
 * @property courseId
 * @property id
 * @property name
 * @property order
 * @property parentChapterId
 * @property userControlSetTop
 * @property visible
 * @constructor Create empty System response
 */
@Parcelize
data class SystemResponse(
    var children: ArrayList<ClassifyResponse>,
    var courseId: Int,
    var id: Int,
    var name: String,
    var order: Int,
    var parentChapterId: Int,
    var userControlSetTop: Boolean,
    var visible: Int
) : Parcelable
