package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Classify response
 *
 * @property children
 * @property courseId
 * @property id
 * @property name
 * @property order
 * @property parentChapterId
 * @property userControlSetTop
 * @property visible
 * @constructor Create empty Classify response
 */
@Parcelize
data class ClassifyResponse(
    var children: List<String> = listOf(),
    var courseId: Int = 0,
    var id: Int = 0,
    var name: String = "",
    var order: Int = 0,
    var parentChapterId: Int = 0,
    var userControlSetTop: Boolean = false,
    var visible: Int = 0
) : Parcelable