package com.simplation.learnviewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Author: Simplation.WANG
 * Date: 2021/01/25 22:07
 * Description:
 *  ViewModel生命周期特性：ViewModel独立于配置变化。这意味着，屏幕旋转所导致的Activity重建，并不会影响ViewModel的生命周期。
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}