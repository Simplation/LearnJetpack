package com.simplation.mvvm.app.event

import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.simplation.mvvm.app.util.CacheUtil
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.data.model.bean.UserInfo
import com.simplation.mvvmlib.base.appContext
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.callback.livedata.event.EventLiveData

/**
 * @作者: Simplation
 * @日期: 2021/7/12 17:47
 * @描述:
 * @更新:
 */
class AppViewModel : BaseViewModel() {

    // App 的账户信息
    var userInfo = UnPeekLiveData.Builder<UserInfo>().setAllowNullValue(true).create()

    // App 主题颜色 中大型项目不推荐以这种方式改变主题颜色，比较繁琐耦合，且容易有遗漏某些控件没有设置主题色
    var appColor = EventLiveData<Int>()

    // App 列表动画
    var appAnimation = EventLiveData<Int>()

    init {
        // 默认值保存的账户信息，没有登陆过则为 null
        userInfo.value = CacheUtil.getUser()
        // 默认值颜色
        appColor.value = SettingUtil.getColor(appContext)
        // 初始化列表动画
        appAnimation.value = SettingUtil.getListMode()
    }
}