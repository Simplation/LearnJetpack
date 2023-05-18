package com.simplation.learndatabinding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.simplation.learndatabinding.databinding.ActivityBindingMainBinding

class BindingMainActivity : AppCompatActivity() {
    private lateinit var mDataBinding: ActivityBindingMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_binding_main)

        val book = Book("Android 进阶之光", "刘望舒", "5", 5)
        mDataBinding.book = book


        // mDataBinding.eventHandler = EventHandlerListener(this)

        mDataBinding.netWorkImage = "图片地址"

        mDataBinding.setViewModel(TwoWayBindingViewModel())
    }

    inner class EventHandlerListener(context: Context) {
        private val mContext: Context = context

        fun onButtonClicked() {
            Toast.makeText(mContext, "I am clicked!", Toast.LENGTH_SHORT).show()
        }
    }
}