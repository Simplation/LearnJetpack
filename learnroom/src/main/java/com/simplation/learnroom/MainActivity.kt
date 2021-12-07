package com.simplation.learnroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.simplation.learnroom.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var studentList: MutableList<Student>
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        studentAdapter = StudentAdapter(this, studentList)

        mBinding.tvContent.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = studentAdapter
        }

        initData()

        operateFun()
    }

    private fun initData(): MutableList<Student> {
        studentList = mutableListOf()
        for (i in 0..5) {
            val student = Student(i, "student ${i + 1}", Random.nextInt())
            studentList.add(i, student)
        }
        return studentList
    }

    private fun operateFun() {
        val database = MyDatabase.getInstance(this)

        // 增加
        database.studentDao().insertStudent(Student("name1", 18))

        // 删除
        database.studentDao().deleteStudent(Student("name1", 18))

        // 更新
        database.studentDao().updateStudent(Student("name1", 21))

        // 查询
        /*val students = database.studentDao().getStudentList()
        for (student in students) {
            println("result student is $student")
        }*/


        val studentViewModel = ViewModelProvider(this)[StudentViewModel::class.java]
        studentViewModel.getLiveDataStudent().observe(this, {
            fun onChanged(students: List<Student>) {
                studentList.clear()
                studentList.addAll(students)
                studentAdapter.notifyDataSetChanged()
            }
        })

        // 根据 ID 进行查询
        val student = database.studentDao().getStudentById(1)
    }
}