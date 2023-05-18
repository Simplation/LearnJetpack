package com.simplation.mvvm.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.simplation.mvvm.app.ext.dismissLoadingExt
import com.simplation.mvvm.app.ext.showLoadingExt
import com.simplation.mvvmlib.base.fragment.BaseVmDbFragment
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel

/**
 * Base fragment
 *
 * @constructor Create empty Base fragment
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbFragment<VM, DB>() {
    abstract override fun layoutId(): Int

    abstract override fun initView(savedInstanceState: Bundle?)

    override fun lazyLoadData() {}

    override fun createObserver() {}

    override fun initData() {}

    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    override fun dismissLoading() {
        dismissLoadingExt()
    }
}