package com.simplation.mvvmlib.callback.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Un peek live data
 *      仅分发 owner observe 后 才新拿到的数据 可避免共享作用域 VM 下 liveData 被 observe 时旧数据倒灌的情况
 *
 * @param T
 * @constructor Create empty Un peek live data
 */
open class UnPeekLiveData<T> : MutableLiveData<T>() {
    override fun observe(
        owner: LifecycleOwner,
        observer: Observer<in T>
    ) {
        super.observe(owner, observer)
        hook(observer)
    }

    private fun hook(observer: Observer<in T>) {
        val liveDataClass = LiveData::class.java
        try {
            // 获取 field private SafeIterableMap<Observer<T>, ObserverWrapper> mObservers
            val mObservers = liveDataClass.getDeclaredField("mObservers")
            mObservers.isAccessible = true
            // 获取 SafeIterableMap 集合 mObservers
            val observers = mObservers[this]
            val observersClass: Class<*> = observers.javaClass
            // 获取 SafeIterableMap 的 get(Object obj) 方法
            val methodGet = observersClass.getDeclaredMethod("get", Any::class.java)
            methodGet.isAccessible = true
            // 获取到 observer 在集合中对应的 ObserverWrapper 对象
            val objectWrapperEntry = methodGet.invoke(observers, observer)
            var objectWrapper: Any? = null
            if (objectWrapperEntry is Map.Entry<*, *>) {
                objectWrapper = objectWrapperEntry.value
            }
            if (objectWrapper == null) {
                throw NullPointerException("ObserverWrapper can not be null")
            }
            // 获取 ObserverWrapper 的 Class 对象  LifecycleBoundObserver extends ObserverWrapper
            val wrapperClass: Class<*>? = objectWrapper.javaClass.superclass
            // 获取 ObserverWrapper 的 field mLastVersion
            val mLastVersion =
                wrapperClass!!.getDeclaredField("mLastVersion")
            mLastVersion.isAccessible = true
            // 获取 liveData 的 field mVersion
            val mVersion = liveDataClass.getDeclaredField("mVersion")
            mVersion.isAccessible = true
            val mV = mVersion[this]
            // 把当前 ListData 的 mVersion 赋值给 ObserverWrapper 的 field mLastVersion
            mLastVersion[objectWrapper] = mV
            mObservers.isAccessible = false
            methodGet.isAccessible = false
            mLastVersion.isAccessible = false
            mVersion.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
