package com.simplation.mvvmlib.network.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.simplation.mvvmlib.network.NetworkUtil

/**
 * Network state receiver
 *      网络变化接收器
 *
 * @constructor Create empty Network state receiver
 */
class NetworkStateReceiver : BroadcastReceiver() {
    var isInit = true

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            if (!isInit) {
                if (!NetworkUtil.isNetworkAvailable(context)) {
                    // 收到有网络时判断之前的值是不是没有网络，如果没有网络才提示通知 ，防止重复通知
                    NetworkStateManager.instance.mNetworkStateCallback.value?.let {
                        // 无网络
                        if (it.isSuccess) {
                            NetworkStateManager.instance.mNetworkStateCallback.value =
                                NetState(isSuccess = false)
                        }
                        return
                    }
                    NetworkStateManager.instance.mNetworkStateCallback.value =
                        NetState(isSuccess = false)
                } else {
                    // 收到有网络时判断之前的值是不是没有网络，如果没有网络才提示通知 ，防止重复通知
                    NetworkStateManager.instance.mNetworkStateCallback.value?.let {
                        // 有网络
                        if (!it.isSuccess) {
                            NetworkStateManager.instance.mNetworkStateCallback.value =
                                NetState(isSuccess = true)
                        }
                        return
                    }
                    NetworkStateManager.instance.mNetworkStateCallback.value =
                        NetState(isSuccess = true)
                }
            }
            isInit = false
        }
    }
}
