package com.simplation.learnroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val studentRepository: StudentRepository = StudentRepository(application)

    fun getLiveDataStudent(): LiveData<MutableList<Student>> {
        return studentRepository.getAllStudentsLive()
    }

    fun insert(student: Student) {
        studentRepository.insertStudent(student)
    }

    fun update(student: Student) {
        studentRepository.updateStudent(student)
    }

    fun delete() {
        studentRepository.deleteStudent()
    }

    fun clear() {
        studentRepository.celar()
    }

}