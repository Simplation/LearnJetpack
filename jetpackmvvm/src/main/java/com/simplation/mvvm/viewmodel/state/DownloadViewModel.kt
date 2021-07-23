package com.simplation.mvvm.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.download.DownloadManager
import com.simplation.mvvmlib.ext.download.DownloadResultState
import com.simplation.mvvmlib.ext.download.downLoadExt
import kotlinx.coroutines.launch

/**
 * Download view model
 *
 * @constructor Create empty Download view model
 */
class DownloadViewModel : BaseViewModel() {

    var downloadData: MutableLiveData<DownloadResultState> = MutableLiveData()

    /**
     * Apk 普通下载 框架自带
     * @param path String 文件保存路径
     * @param url String 文件下载 url
     * @param tag String 下载标识，根据该值可取消，暂停
     */
    fun downloadApk(path: String, url: String, tag: String) {
        viewModelScope.launch {
            // 直接强制下载，不管文件是否存在 ，如果需要每次都重新下载可以设置为 true
            DownloadManager.downLoad(tag, url, path, "tmd.apk", false, downLoadExt(downloadData))
        }
    }

    /**
     * 取消下载
     * @param tag String
     */
    fun downloadCancel(tag: String) {
        DownloadManager.cancel(tag)
    }

    /**
     * Apk 暂停下载
     * @param tag String
     */
    fun downloadPause(tag: String) {
        DownloadManager.pause(tag)
    }

}