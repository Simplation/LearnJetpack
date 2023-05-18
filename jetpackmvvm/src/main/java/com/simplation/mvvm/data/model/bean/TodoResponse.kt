package com.simplation.mvvm.data.model.bean

import android.os.Parcelable
import com.simplation.mvvm.app.util.DatetimeUtil
import kotlinx.parcelize.Parcelize

/**
 * Todo response
 *
 * @property completeDate
 * @property completeDateStr
 * @property content
 * @property date
 * @property dateStr
 * @property id
 * @property priority
 * @property status
 * @property title
 * @property type
 * @property userId
 * @constructor Create empty Todo response
 */
@Parcelize
data class TodoResponse(
    var completeDate: Long,
    var completeDateStr: String,
    var content: String,
    var date: Long,
    var dateStr: String,
    var id: Int,
    var priority: Int,
    var status: Int,
    var title: String,
    var type: Int,
    var userId: Int
) : Parcelable {
    fun isDone(): Boolean {
        //判断是否已完成或者已过期
        return if (status == 1) {
            true
        } else {
            DatetimeUtil.now.time > DatetimeUtil.formatDate(
                DatetimeUtil.DATE_PATTERN,
                dateStr
            ).time
        }
    }
}

