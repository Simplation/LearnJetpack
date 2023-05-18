package com.simplation.learnviewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.simplation.learnviewpager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 3

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ScaleFragment()
                    1 -> RotateFragment()
                    else -> TranslateFragment()
                }
            }
        }

        TabLayoutMediator(mainBinding.tabLayout, mainBinding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "缩放"
                1 -> tab.text = "旋转"
                else -> tab.text = "移动"
            }
        }.attach()  // 必须调用 attach() 否则不起作用
    }
}