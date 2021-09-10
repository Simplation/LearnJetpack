package com.simplation.learnroom

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * @作者: Simplation
 * @日期: 2021/09/03 17:31
 * @描述:
 * @更新:
 */
@Dao
interface ColorDao {

    @Query("SELECT * FROM colors")
    fun getAll(): Array<Color>

    @Query("SELECT * FROM colors WHERE name =:name")
    fun getColorByName(name: String): LiveData<Color>

    @Query("SELECT * FROM colors WHERE hex =:hex")
    fun getColorByHex(hex: String): LiveData<Color>

    @Insert
    fun insert(vararg color: Color)

    @Update
    fun update(color: Color)

    @Delete
    fun delete(color: Color)
}