package com.simplation.learnroom

import androidx.room.Entity

/**
 * @作者: Simplation
 * @日期: 2021/09/03 17:30
 * @描述:
 * @更新:
 */
@Entity(tableName = "colors")
data class Color(
    val hex: String,
    val name: String
)
