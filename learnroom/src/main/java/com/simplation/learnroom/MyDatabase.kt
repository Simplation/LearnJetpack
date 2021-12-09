package com.simplation.learnroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    companion object {
        private const val DATA_BASE = "my_db"
        private var databaseInstance: MyDatabase? = null
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("")  // 根据业务需要添加相应的 SQL 语句
                // database.execSQL("ALTER TABLE my_db ADD COLUMN bar_data INTEGER NOT NULL DEFAULT 1")
            }
        }

        //region 删除数据库中的表某一列
        private val MIGRATION_DROP_ROW = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1.创建新表
                database.execSQL("CREATE TABLE WORD_TEMP(id INTEGER PRIMARY KEY NOT NULL, english_word TEXT, chinese_meaning TEXT)")
                // 2.查找并插入新表所需要的数据
                database.execSQL("INSERT INTO WORD_TEMP(id, english_word, chinese_meaning) SELECT id, english_word, chinese_meaning FROM WORD")
                // 3.删除旧表
                database.execSQL("DROP TABLE WORD")
                // 4.对创建的新表进行重命名
                database.execSQL("ALTER TABLE WORD_TEMP RENAME TO WORD")
            }
        }
        //endregion

        fun getInstance(context: Context): MyDatabase {
            if (databaseInstance == null) {
                databaseInstance =
                        Room.databaseBuilder(
                                context.applicationContext,
                                MyDatabase::class.java,
                                DATA_BASE
                        )
                                // .allowMainThreadQueries()            允许在主线程中操作
                                // .fallbackToDestructiveMigration()    破坏性迁移的操作，不建议
                                .addMigrations(MIGRATION_1_2)
                                .build()
            }

            return databaseInstance as MyDatabase
        }

    }

    abstract fun studentDao(): StudentDao
}