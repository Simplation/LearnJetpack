package com.simplation.learnviewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.simplation.learnviewmodel.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initComponent()
    }

    private fun initComponent() {
        val timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)
        timerViewModel.setOnTimeChangeListener(object : TimerViewModel.OnTimeChangeListener {
            override fun onTimeChanged(second: Int) {
                runOnUiThread {
                    Runnable {
                        mBinding.textView.text = "TIME: $second"
                    }
                }
            }

        })
        timerViewModel.startTiming()
    }
}