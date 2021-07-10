package com.simplation.learnroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private var myDatabase: MyDatabase = MyDatabase.getInstance(application)
    private var liveDataStudent: LiveData<List<Student>> = myDatabase.studentDao().getStudentList()

    fun getLiveDataStudent(): LiveData<List<Student>> {
        return liveDataStudent
    }
}