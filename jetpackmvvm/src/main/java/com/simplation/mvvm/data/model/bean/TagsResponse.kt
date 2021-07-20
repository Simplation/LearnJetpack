package com.simplation.mvvm.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Tags response  文章的标签
 *
 * @property name
 * @property url
 * @constructor Create empty Tags response
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class TagsResponse(var name:String, var url:String): Parcelable