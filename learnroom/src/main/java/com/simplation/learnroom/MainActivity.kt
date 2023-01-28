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
    private lateinit var studentViewModel: StudentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initData()

        studentViewModel = ViewModelProvider(this)[StudentViewModel::class.java]
        studentAdapter = StudentAdapter(this, studentList)

        mBinding.tvContent.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = studentAdapter
        }

        mBinding.buttonAdd.setOnClickListener {
            studentViewModel.insert(Student("name1", 18))
        }

        mBinding.buttonUpdate.setOnClickListener {
            studentViewModel.update(Student("name1", 18))
        }

        mBinding.buttonDelete.setOnClickListener {
            studentViewModel.delete()
        }

        mBinding.buttonClear.setOnClickListener {
            studentViewModel.clear()
        }
    }

    /**
     * 数据初始化
     */
    private fun initData(): MutableList<Student> {
        studentList = mutableListOf()
        for (i in 0..10) {
            val student = Student(i, "student ${i + 1}", Random.nextInt())
            studentList.add(i, student)
        }
        return studentList
    }
}