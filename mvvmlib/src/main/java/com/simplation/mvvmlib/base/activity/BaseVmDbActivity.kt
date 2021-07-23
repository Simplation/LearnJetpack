package com.simplation.mvvmlib.base.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel

/**
 * Base vm db activity
 *      包含 ViewModel 和 Databind ViewModelActivity 基类，把 ViewModel 和 Databind 注入进来了
 * 需要使用 Databind 的请继承它
 * @param VM
 * @param DB
 * @constructor Create empty Base vm db activity
 */
abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmActivity<VM>() {
    lateinit var mDatabind: DB

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
