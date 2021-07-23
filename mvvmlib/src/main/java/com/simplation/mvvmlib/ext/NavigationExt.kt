package com.simplation.mvvmlib.ext

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.simplation.mvvmlib.navigation.NavHostFragment
import java.lang.Exception

fun Fragment.nav(): NavController {
    return NavHostFragment.findNavController(this)
}

fun nav(view: View): NavController {
    return Navigation.findNavController(view)
}

var lastNavTime = 0L


/**
 * 防止短时间内多次快速跳转 Fragment 出现的 bug
 * @param resId 跳转的 action Id
 * @param bundle 传递的参数
 * @param interval 多少毫秒内不可重复点击 默认 0.5 秒
 */
fun NavController.navigateAction(resId: Int, bundle: Bundle? = null, interval: Long = 500) {
    val currentTime = System.currentTimeMillis()
    if (currentTime >= lastNavTime + interval) {
        lastNavTime = currentTime
        try {
            navigate(resId, bundle)
        } catch (ingored: Exception) {
            // ingored.printStackTrace()
            // 防止出现 当 fragment 中 action 的 duration 设置为 0 时，连续点击两个不同的跳转会导致如下崩溃 #issue53
        }
    }
}
