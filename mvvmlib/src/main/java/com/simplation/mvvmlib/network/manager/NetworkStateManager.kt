package com.simplation.mvvmlib.network.manager

import com.simplation.mvvmlib.callback.livedata.event.EventLiveData

/**
 * @作者: Simplation
 * @日期: 2021/4/20 16:28
 * @描述: 管理网络变化
 * @更新:
 */
class NetworkStateManager private constructor() {
    val mNetworkStateCallback = EventLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }
}