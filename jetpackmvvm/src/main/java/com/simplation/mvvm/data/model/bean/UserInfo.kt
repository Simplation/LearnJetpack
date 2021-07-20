package com.simplation.mvvm.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * User info
 *
 * @property admin
 * @property chapterTops
 * @property collectIds
 * @property email
 * @property icon
 * @property id
 * @property nickname
 * @property password
 * @property token
 * @property type
 * @property username
 * @constructor Create empty User info
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class UserInfo(
    var admin: Boolean = false,
    var chapterTops: List<String> = listOf(),
    var collectIds: MutableList<String> = mutableListOf(),
    var email: String = "",
    var icon: String = "",
    var id: String = "",
    var nickname: String = "",
    var password: String = "",
    var token: String = "",
    var type: Int = 0,
    var username: String = ""
) : Parcelable