package com.simplation.mvvm.viewmodel.state

import com.simplation.mvvm.app.util.ColorUtil
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.callback.databind.IntObservableField
import com.simplation.mvvmlib.callback.databind.StringObservableField
import com.simplation.mvvmlib.callback.livedata.UnPeekLiveData

/**
 * Me view model
 *      用于存储 MeFragment 界面数据的 ViewModel
 *
 * @constructor Create empty Me view model
 */
class MeViewModel : BaseViewModel() {

    var name = StringObservableField("请先登录...")

    var integral = IntObservableField(0)

    var info = StringObservableField("id：--　排名：-")

    var imageUrl = StringObservableField(ColorUtil.randomImage())

    var testString = UnPeekLiveData<String>()

}
