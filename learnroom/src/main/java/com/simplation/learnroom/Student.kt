package com.simplation.learnroom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "student")
class Student {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    var id = 0

    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    var name: String? = null

    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    var age: Int = 0

    //region Room 默认会使用这个构造器操作数据
    constructor(id: Int, name: String?, age: Int) {
        this.id = id
        this.name = name
        this.age = age
    }
    //endregion

    //endregion
    //region 由于 Room 只能识别一个构造函数，如果希望定义多个构造函数，可以使用 Ignore 标签，让 Room 忽略这个构造器
    // Ignore也可以用于字段, Room 不会持久化被 @Ignore 标签标记过的字段的数据
    @Ignore
    constructor(name: String?, age: Int?) {
        this.name = name
        if (age != null) {
            this.age = age
        }
    }
    //endregion
}