package com.simplation.learnroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Student::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    companion object {

        val DATABASE = "my_db"
        private var databaseInstance: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase {
            if (databaseInstance == null) {
                databaseInstance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        DATABASE
                    )
                        .build()
            }

            return databaseInstance as MyDatabase
        }

    }

    abstract fun studentDao(): StudentDao
}