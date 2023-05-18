package com.simplation.mvvmlib.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.getVmClazz
import com.simplation.mvvmlib.network.manager.NetState
import com.simplation.mvvmlib.network.manager.NetworkStateManager

/**
 * Base vm activity
 *      ViewModelActivity 基类，把 ViewModel 注入进来了
 *
 * @param VM
 * @constructor Create empty Base vm activity
 */
abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity() {

    /**
     * 是否需要使用 DataBinding 供子类 BaseVmDbActivity 修改，用户请慎动
     */
    private var isUserDb = false

    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun showLoading(message: String = "请求网络中...")

    abstract fun dismissLoading()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isUserDb) {
            setContentView(layoutId())
        } else {
            initDataBind()
        }
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observe(this) {
            onNetworkStateChanged(it)
        }
    }

    /**
     * On network state changed
     * 网络变化监听 子类重写
     *
     * @param netState
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 创建 LiveData 数据观察者
     */
    abstract fun createObserver()

    /**
     * 注册 UI 事件
     */
    private fun registerUiChange() {
        // 显示弹窗
        mViewModel.loadingChange.showDialog.observe(this) {
            showLoading(it)
        }

        // 关闭弹窗
        mViewModel.loadingChange.dismissDialog.observe(this) {
            dismissLoading()
        }
    }

    /**
     * 将非该 Activity 绑定的 ViewModel 添加 loading 回调 防止出现请求时不显示 loading 弹窗 bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel) {
        viewModels.forEach { viewModel ->
            // 显示弹窗
            viewModel.loadingChange.showDialog.observe(this) {
                showLoading(it)
            }
            // 关闭弹窗
            viewModel.loadingChange.dismissDialog.observe(this) {
                dismissLoading()
            }
        }
    }

    /**
     * Create view model
     *
     * @return
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    fun userDataBinding(isUserDb: Boolean) {
        this.isUserDb = isUserDb
    }

    /**
     * 供子类 BaseVmDbActivity 初始化 Databinding 操作
     */
    open fun initDataBind() {}
}