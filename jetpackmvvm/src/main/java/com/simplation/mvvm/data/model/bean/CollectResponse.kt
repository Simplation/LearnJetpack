package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Collect response
 *
 * @property chapterId
 * @property author
 * @property chapterName
 * @property courseId
 * @property desc
 * @property envelopePic
 * @property id
 * @property link
 * @property niceDate
 * @property origin
 * @property originId
 * @property publishTime
 * @property title
 * @property userId
 * @property visible
 * @property zan
 * @constructor Create empty Collect response
 */
@Parcelize
data class CollectResponse(
    var chapterId: Int,
    var author: String,
    var chapterName: String,
    var courseId: Int,
    var desc: String,
    var envelopePic: String,
    var id: Int,
    var link: String,
    var niceDate: String,
    var origin: String,
    var originId: Int,
    var publishTime: Long,
    var title: String,
    var userId: Int,
    var visible: Int,
    var zan: Int
) : Parcelable