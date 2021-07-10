package com.simplation.mvvmlib.base.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel

/**
 * @作者: Simplation
 * @日期: 2021/4/23 10:03
 * @描述: 包含 ViewModel 和 Databind ViewModelActivity 基类，把 ViewModel 和 Databind 注入进来了
 * 需要使用 Databind 的请继承它
 * @更新:
 */

abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmActivity<VM>() {
    private lateinit var mDatabind: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        userDataBinding(true)
        super.onCreate(savedInstanceState)
    }

    /**
     * 创建 DataBinding
     */
    override fun initDataBind() {
        mDatabind = DataBindingUtil.setContentView(this, layoutId())
        mDatabind.lifecycleOwner = this
    }
}
