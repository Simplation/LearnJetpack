package com.simplation.mvvmlib.base

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.IntentFilter
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import androidx.lifecycle.ProcessLifecycleOwner
import com.simplation.mvvmlib.ext.lifecycle.KtxAppLifeObserver
import com.simplation.mvvmlib.ext.lifecycle.KtxLifeCycleCallBack
import com.simplation.mvvmlib.network.manager.NetworkStateReceiver

/**
 * @作者: Simplation
 * @日期: 2021/7/9 10:07
 * @描述:
 * @更新:
 */

val appContext: Application by lazy { Ktx.app }


class Ktx : ContentProvider() {
    companion object {
        lateinit var app: Application
        private var mNetworkStateReceive: NetworkStateReceiver? = null
        var watchActivityLife = true
        var watchAppLife = true
    }

    override fun onCreate(): Boolean {
        val application = context!!.applicationContext as Application
        install(application)
        return true
    }

    private fun install(application: Application) {
        app = application
        mNetworkStateReceive = NetworkStateReceiver()
        app.registerReceiver(
            mNetworkStateReceive,
            IntentFilter(ConnectivityManager.EXTRA_NO_CONNECTIVITY)
        )

        if (watchActivityLife) application.registerActivityLifecycleCallbacks(KtxLifeCycleCallBack())
        if (watchAppLife) ProcessLifecycleOwner.get().lifecycle.addObserver(KtxAppLifeObserver)
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0
}
