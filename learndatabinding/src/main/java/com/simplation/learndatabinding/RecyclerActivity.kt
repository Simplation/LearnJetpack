package com.simplation.learndatabinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.simplation.learndatabinding.databinding.ActivityRecyvlerBinding

class RecyclerActivity : AppCompatActivity() {
    private lateinit var mDataBinding: ActivityRecyvlerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_recyvler)

        mDataBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mDataBinding.recyclerView.setHasFixedSize(true)
        val adapter =  RecyclerViewAdapter(RecyclerViewViewModel.getBooks())
        mDataBinding.recyclerView.adapter = adapter
    }
}