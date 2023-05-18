package com.simplation.mvvm.ui.fragment.demo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.simplation.mvvm.R
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.app.ext.showMessage
import com.simplation.mvvm.databinding.FragmentDownloadBinding
import com.simplation.mvvm.viewmodel.state.DownloadViewModel
import com.simplation.mvvmlib.ext.download.DownloadResultState
import com.simplation.mvvmlib.ext.download.FileTool
import com.simplation.mvvmlib.ext.download.FileTool.getBasePath
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.util.logd

/**
 * Down load fragment
 *      文件下载 示例 框架自带的，功能比较简单，没有三方库那么强大
 *
 * @constructor Create empty Down load fragment
 */
class DownLoadFragment  : BaseFragment<DownloadViewModel, FragmentDownloadBinding>() {

    private lateinit var toolBar:Toolbar

    override fun layoutId() = R.layout.fragment_download

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        mDataBind.click = ProxyClick()

        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.initClose("框架自带普通下载") {
            nav().navigateUp()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun createObserver() {
        mViewModel.downloadData.observe(viewLifecycleOwner, {
            when (it) {
                is DownloadResultState.Pending -> {
                    // 开始下载
                    "开始下载".logd()
                }
                is DownloadResultState.Progress -> {
                    // 下载中
                    mDataBind.downloadProgressBar.progress = it.progress
                    "下载中 ${it.soFarBytes}/${it.totalBytes}".logd()
                    mDataBind.downloadProgress.text = "${it.progress}%"
                    mDataBind.downloadSize.text =
                        "${FileTool.bytes2kb(it.soFarBytes)}/${FileTool.bytes2kb(it.totalBytes)}"
                }
                is DownloadResultState.Success -> {
                    // 下载成功
                    mDataBind.downloadProgressBar.progress = 100
                    mDataBind.downloadProgress.text = "100%"
                    mDataBind.downloadSize.text =
                        "${FileTool.bytes2kb(it.totalBytes)}/${FileTool.bytes2kb(it.totalBytes)}"
                    showMessage("下载成功--文件地址：${it.filePath}")
                }
                is DownloadResultState.Pause -> {
                    showMessage("下载暂停")
                }
                is DownloadResultState.Error -> {
                    // 下载失败
                    showMessage("错误信息:${it.errorMsg}")
                }
            }
        })
    }

    inner class ProxyClick {
        fun download() {
            // 普通下载
            mViewModel.downloadApk(
                getBasePath(),
                "https://down.qq.com/qqweb/QQlite/Android_apk/qqlite_4.0.1.1060_537064364.apk",
                "qq"
            )
        }

        fun cancel() {
            mViewModel.downloadCancel("qq")
        }

        fun pause() {
            mViewModel.downloadPause("qq")
        }
    }
}
