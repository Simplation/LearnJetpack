package com.simplation.mvvm.ui.fragment

import android.os.Bundle
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.init
import com.simplation.mvvm.app.ext.initMain
import com.simplation.mvvm.app.ext.interceptLongClick
import com.simplation.mvvm.app.ext.setUiTheme
import com.simplation.mvvm.databinding.FragmentMainBinding
import com.simplation.mvvm.viewmodel.state.MainViewModel

/**
 * Main fragment
 *
 * @constructor Create empty Main fragment
 */
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    override fun layoutId(): Int = R.layout.fragment_main

    override fun initView(savedInstanceState: Bundle?) {
        // 初始化 viewpager2
        mDataBind.mainViewpager.initMain(this)

        // 初始化 bottomBar
        mDataBind.mainBottom.init {
            when (it) {
                R.id.menu_main -> mDataBind.mainViewpager.setCurrentItem(0, false)
                R.id.menu_project -> mDataBind.mainViewpager.setCurrentItem(1, false)
                R.id.menu_system -> mDataBind.mainViewpager.setCurrentItem(2, false)
                R.id.menu_public -> mDataBind.mainViewpager.setCurrentItem(3, false)
                R.id.menu_me -> mDataBind.mainViewpager.setCurrentItem(4, false)
            }
        }

        mDataBind.mainBottom.interceptLongClick(
            R.id.menu_main,
            R.id.menu_project,
            R.id.menu_system,
            R.id.menu_public,
            R.id.menu_me
        )
    }

    override fun createObserver() {
        super.createObserver()

        appViewModel.appColor.observe(this) {
            setUiTheme(it, mDataBind.mainBottom)
        }
    }
}