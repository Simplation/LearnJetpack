package com.simplation.learnroom

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @作者: Simplation
 * @日期: 2021/12/09 9:14
 * @描述:
 * @更新:
 */
@DelicateCoroutinesApi
class StudentRepository(context: Context) {
    private var allStudentsLive: LiveData<MutableList<Student>>
    private var studentDao: StudentDao

    init {
        val myDatabase = MyDatabase.getInstance(context.applicationContext)
        studentDao = myDatabase.studentDao()
        allStudentsLive = studentDao.getStudentList()
    }

    fun getAllStudentsLive(): LiveData<MutableList<Student>> {
        return allStudentsLive
    }

    fun insertStudent(student: Student) {
        GlobalScope.launch(Dispatchers.Main) {
            val result = launch(Dispatchers.IO) {
                studentDao.insertStudent(student)
            }
            Log.d("StudentRepository", "insertStudent: $result")
        }
    }

    fun updateStudent(student: Student) {
        GlobalScope.launch(Dispatchers.Main) {
            val result = launch(Dispatchers.IO) {
                studentDao.updateStudent(student)
            }
            Log.d("StudentRepository", "updateStudent: $result")
        }
    }

    fun deleteStudent() {
        GlobalScope.launch(Dispatchers.Main) {
            val result = launch(Dispatchers.IO) {
                val student = Student("Simplation", 18)
                studentDao.deleteStudent(student)
            }
            Log.d("StudentRepository", "deleteStudent: $result")
        }
    }

    fun celar() {
        GlobalScope.launch(Dispatchers.Main) {
            val result = launch(Dispatchers.IO) {
                studentDao.deleteAllStudents()
            }
            Log.d("StudentRepository", "celar: $result")
        }
    }
}