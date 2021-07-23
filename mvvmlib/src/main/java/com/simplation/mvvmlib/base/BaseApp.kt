package com.simplation.mvvmlib.base

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * Base app
 *
 * @constructor Create empty Base app
 */
open class BaseApp : Application(), ViewModelStoreOwner {

    private lateinit var mViewModelStore: ViewModelStore
    private var mFactory: ViewModelProvider.Factory? = null

    override fun onCreate() {
        super.onCreate()

        mViewModelStore = ViewModelStore()
    }

    override fun getViewModelStore(): ViewModelStore {
        return mViewModelStore
    }

    /**
     * 获取全局的 ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    /**
     * Get app factory
     *
     * @return
     */
    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }
}