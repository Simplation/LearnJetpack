package com.simplation.learnroom

import android.os.AsyncTask
import kotlinx.coroutines.*

//class QueryStudentTask : AsyncTask<Void, Void, Void>() {
//
//    val myDatabase: MyDatabase? = null
//    private val studentAdapter: StudentAdapter? = null
//    private val studentList = null
//
//    override fun doInBackground(vararg params: Void): Void? {
//
//        studentList?.clear()
////        studentList?.addAll(myDatabase?.studentDao().getStudentList())
//        return null
//    }
//
//    override fun onPostExecute(result: Void) {
//        super.onPostExecute(result)
//
//        studentAdapter?.notifyDataSetChanged()
//    }
//
//}

class QueryStudentTask {

    private val myDatabase: MyDatabase? = null
    private val studentAdapter: StudentAdapter? = null
    private val studentList = mutableListOf<Student>()

    val job = GlobalScope.launch(Dispatchers.IO) {
        studentList.clear()
        studentList.add(myDatabase?.studentDao()?.getStudentById(1)!!)
        // studentList.addAll(myDatabase?.studentDao().getStudentList())
        studentAdapter?.notifyDataSetChanged()
    }
}