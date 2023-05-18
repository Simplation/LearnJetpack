package com.simplation.learnroom

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StudentDao {
    @Insert
    fun insertStudent(student: Student)

    @Delete
    fun deleteStudent(student: Student)

    @Update
    fun updateStudent(vararg student: Student)

    @Query("SELECT * FROM student")
    fun getStudentList(): LiveData<MutableList<Student>>

    @Query("SELECT * FROM student WHERE id = :id")
    fun getStudentById(id: Int): Student

    @Query("DELETE FROM STUDENT")
    fun deleteAllStudents()
}