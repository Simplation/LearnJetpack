package com.simplation.mvvm.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.simplation.mvvm.app.ext.dismissLoadingExt
import com.simplation.mvvm.app.ext.showLoadingExt
import com.simplation.mvvmlib.base.activity.BaseVmDbActivity
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel

/**
 * Base activity
 * 你项目中的 Activity 基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作，如果不想用 Databind，请继承 BaseVmActivity
 * 例如： abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {
 *
 * @constructor Create empty Base activity
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {
    abstract override fun layoutId(): Int

    abstract override fun initView(savedInstanceState: Bundle?)

    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    override fun dismissLoading() {
        dismissLoadingExt()
    }

    /**
     * 创建 LiveData 观察者
     */
    override fun createObserver() {

    }

    /**
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     */
    /*override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }*/

}
