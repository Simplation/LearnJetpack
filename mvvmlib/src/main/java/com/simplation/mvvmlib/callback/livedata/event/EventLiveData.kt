package com.simplation.mvvmlib.callback.livedata.event

import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * Event live data
 *      最新版的发送消息 LiveData 使用 https://github.com/KunMinX/UnPeek-LiveData 的最新版，
 * 因为跟其他类名（UnPeekLiveData）一致 所以继承换了一个名字在 Activity 中 observe 调用 observeInActivity
 * 在 Fragment 中使用调用 observeInFragment, 具体写法请参考 https://github.com/KunMinX/UnPeek-LiveData 的示例
 *
 * @param T
 * @constructor Create empty Event live data
 */
class EventLiveData<T> : UnPeekLiveData<T>()