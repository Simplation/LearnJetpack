package com.simplation.mvvm.ui.fragment.demo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.liulishuo.filedownloader.FileDownloader
import com.simplation.mvvm.R
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.app.ext.showMessage
import com.simplation.mvvm.databinding.FragmentDownloadLibraryBinding
import com.simplation.mvvm.viewmodel.state.DownloadLibraryViewModel
import com.simplation.mvvmlib.base.appContext
import com.simplation.mvvmlib.ext.download.DownloadResultState
import com.simplation.mvvmlib.ext.download.FileTool
import com.simplation.mvvmlib.ext.download.FileTool.getBasePath
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.util.logd

/**
 * Down load library fragment
 *      集成了 GitHub 高 star 的一个下载库 https://github.com/lingochamp/FileDownloader
 *
 * @constructor Create empty Down load library fragment
 */
class DownLoadLibraryFragment :
    BaseFragment<DownloadLibraryViewModel, FragmentDownloadLibraryBinding>() {

    private lateinit var toolBar: Toolbar

    override fun layoutId() = R.layout.fragment_download_library

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        mDataBind.click = ProxyClick()
        // 第三方下载库注册， 可以直接放在 application 里面注册
        FileDownloader.setup(appContext)
        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.initClose("三方库下载") {
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
                    mDataBind.downloadLibraryProgressBar.progress = it.progress
                    "下载中 ${it.soFarBytes}/${it.totalBytes}".logd()
                    mDataBind.downloadLibraryProgress.text = "${it.progress}%"
                    mDataBind.downloadLibrarySize.text =
                        "${FileTool.bytes2kb(it.soFarBytes)}/${FileTool.bytes2kb(it.totalBytes)}"
                }
                is DownloadResultState.Success -> {
                    // 下载成功
                    mDataBind.downloadLibraryProgressBar.progress = 100
                    mDataBind.downloadLibraryProgress.text = "100%"
                    mDataBind.downloadLibrarySize.text =
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
        fun downloadLibrary() {
            // 测试一下利用三方库下载
            mViewModel.downloadApkByLibrary(
                getBasePath(),
                "https://down.qq.com/qqweb/QQlite/Android_apk/qqlite_4.0.1.1060_537064364.apk",
                "qq"
            )
        }

        fun cancel() {
            mViewModel.downloadCancel()
        }

        fun pause() {
            mViewModel.downloadPause()
        }
    }

}
