package com.simplation.learnroom

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @作者: Simplation
 * @日期: 2021/09/03 17:30
 * @描述:
 * @更新:
 */
@Entity(tableName = "colors")
data class Color(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val hex: String,
    val name: String
)
