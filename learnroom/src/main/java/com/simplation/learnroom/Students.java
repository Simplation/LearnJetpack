//package com.simplation.learnroom;
//
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;
//
//@Entity(tableName = "student")
//public class Students {
//
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
//    private int id;
//
//    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
//    private final String name;
//
//    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.TEXT)
//    private final String age;
//
//    //region Room 默认会使用这个构造器操作数据
//    public Students(int id, String name, String age) {
//        this.id = id;
//        this.name = name;
//        this.age = age;
//    }
//    //endregion
//
//    //region 由于 Room 只能识别一个构造函数，如果希望定义多个构造函数，可以使用 Ignore 标签，让 Room 忽略这个构造器
//    // Ignore也可以用于字段, Room 不会持久化被 @Ignore 标签标记过的字段的数据
//    @Ignore
//    public Students(String name, String age) {
//        this.name = name;
//        this.age = age;
//    }
//    //endregion
//}
//
