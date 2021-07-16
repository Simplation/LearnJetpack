package com.simplation.mvvm.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Article response
 *
 * @property apkLink
 * @property author
 * @property chapterId
 * @property chapterName
 * @property collect
 * @property courseId
 * @property desc
 * @property envelopePic
 * @property fresh
 * @property id
 * @property link
 * @property niceDate
 * @property origin
 * @property prefix
 * @property projectLink
 * @property publishTime
 * @property superChapterId
 * @property superChapterName
 * @property shareUser
 * @property tags
 * @property title
 * @property type
 * @property userId
 * @property visible
 * @property zan
 * @constructor Create empty Article response
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class ArticleResponse(
    var apkLink: String,
    var author: String,   // 作者
    var chapterId: Int,
    var chapterName: String,
    var collect: Boolean, // 是否收藏
    var courseId: Int,
    var desc: String,
    var envelopePic: String,
    var fresh: Boolean,
    var id: Int,
    var link: String,
    var niceDate: String,
    var origin: String,
    var prefix: String,
    var projectLink: String,
    var publishTime: Long,
    var superChapterId: Int,
    var superChapterName: String,
    var shareUser: String,
    var tags: List<TagsResponse>,
    var title: String,
    var type: Int,
    var userId: Int,
    var visible: Int,
    var zan: Int
) : Parcelable
