package com.simplation.mvvm.app.event

import com.simplation.mvvm.data.model.bean.CollectBus
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.callback.livedata.event.EventLiveData

/**
 * APP 全局的 ViewModel，可以在这里发送全局通知替代 EventBus，LiveDataBus 等
 *
 * @constructor Create empty Event view model
 */
class EventViewModel : BaseViewModel() {
    // 全局收藏，在任意一个地方收藏或取消收藏，监听该值的界面都会收到消息
    val collectEvent = EventLiveData<CollectBus>()

    // 分享文章通知
    val shareArticleEvent = EventLiveData<Boolean>()

    // 添加TODO 通知
    val todoEvent = EventLiveData<Boolean>()
}