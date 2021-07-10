package com.simplation.mvvmlib.ext.lifecycle

import android.app.Activity
import java.util.*

/**
 * @作者: Simplation
 * @日期: 2021/4/20 16:48
 * @描述:
 * @更新:
 */

object KtxActivityManger {
    private val mActivityList = LinkedList<Activity>()

    private val currentActivity: Activity?
        get() = if (mActivityList.isEmpty()) null
        else mActivityList.last

    /**
     * Activity 入栈
     */
    fun pushActivity(activity: Activity) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.add(activity)
            }
        } else {
            mActivityList.add(activity)
        }
    }

    /**
     * Activity 出栈
     */
    fun popActivity(activity: Activity) {
        mActivityList.remove(activity)
    }

    /**
     * 关闭当前 Activity
     */
    fun finishCurrentActivity() {
        currentActivity?.finish()
    }

    /**
     * 关闭传入的 Activity
     */
    fun finishActivity(activity: Activity) {
        mActivityList.remove(activity)
        activity.finish()
    }

    /**
     * 关闭传入的 Activity 类名
     */
    fun finishActivity(clazz: Class<*>) {
        for (activity in mActivityList) {
            if (activity.javaClass == clazz) {
                activity.finish()
            }
        }
    }

    /**
     * 关闭所有的 Activity
     */
    fun finishAllActivity() {
        for (activity in mActivityList) {
            activity.finish()
        }
    }

}
