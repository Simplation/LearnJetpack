package com.simplation.mvvmlib.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel

/**
 * @作者: Simplation
 * @日期: 2021/4/23 10:03
 * @描述: ViewModelFragment 基类，自动把 ViewModel 注入 Fragment 和 Databind 注入进来了
 * 需要使用 Databind 的请继承它
 * @更新:
 */

abstract class BaseVmDbFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmFragment<VM>() {
    // 该类绑定的 ViewDataBinding
    lateinit var mDataBind: DB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBind = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        mDataBind.lifecycleOwner = this
        return mDataBind.root
    }
}
