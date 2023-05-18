package com.simplation.mvvmlib.network.manager

import com.simplation.mvvmlib.callback.livedata.event.EventLiveData

/**
 * Network state manager
 *      管理网络变化
 *
 * @constructor Create empty Network state manager
 */
class NetworkStateManager private constructor() {
    val mNetworkStateCallback = EventLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }
}