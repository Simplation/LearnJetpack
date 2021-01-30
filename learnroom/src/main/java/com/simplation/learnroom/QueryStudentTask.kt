package com.simplation.learnroom

import android.os.AsyncTask

class QueryStudentTask : AsyncTask<Void, Void, Void>() {

    val myDatabase: MyDatabase? = null
    val studentAdapter: StudentAdapter? = null
    val studentList = null

    override fun doInBackground(vararg params: Void): Void? {

        studentList?.clear()
//        studentList?.addAll(myDatabase?.studentDao().getStudentList())
        return null
    }

    override fun onPostExecute(result: Void) {
        super.onPostExecute(result)

        studentAdapter?.notifyDataSetChanged()
    }

}