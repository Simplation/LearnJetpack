package com.simplation.learnroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Student::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    companion object {
        private const val DATA_BASE = "my_db"
        private var databaseInstance: MyDatabase? = null
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("")  // 根据业务需要添加相应的 SQL 语句
            }
        }

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