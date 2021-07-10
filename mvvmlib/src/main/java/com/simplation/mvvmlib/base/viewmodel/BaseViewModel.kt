package com.simplation.mvvmlib.base.viewmodel

import androidx.lifecycle.ViewModel
import com.simplation.mvvmlib.callback.livedata.event.EventLiveData

/**
 * @作者: Simplation
 * @日期: 2021/4/22 15:15
 * @描述: ViewModel 的基类 使用 ViewModel 类，放弃 AndroidViewModel，原因：用处不大 完全有其他方式获取 Application 上下文
 * @更新:
 */
open class BaseViewModel : ViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    /**
     * 内置封装好的可通知 Activity/fragment 显示隐藏加载框 因为需要跟网络请求显示隐藏 loading 配套才加的
     */
    inner class UiLoadingChange {
        // 显示加载框
        val showDialog by lazy { EventLiveData<String>() }

        // 取消
        val dismissDialog by lazy { EventLiveData<Boolean>() }
    }
}