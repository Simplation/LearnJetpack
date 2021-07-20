package com.simplation.mvvm.ui.fragment.demo

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.simplation.mvvm.R
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.databinding.FragmentDemoBinding
import com.simplation.mvvm.viewmodel.state.DemoViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction

/**
 * Demo fragment
 *      示例，目前只有 文件下载示例 后面想到什么加什么，作者那个比很懒，佛性添加
 *
 * @constructor Create empty Demo fragment
 */
class DemoFragment : BaseFragment<DemoViewModel, FragmentDemoBinding>() {

    private lateinit var toolBar: Toolbar

    override fun layoutId(): Int = R.layout.fragment_demo

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        mDataBind.click = ProxyClick()

        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.initClose("示例") {
            nav().navigateUp()
        }
    }

    inner class ProxyClick {
        fun download() {
            // 测试一下 普通的下载
            nav().navigateAction(R.id.action_demoFragment_to_downLoadFragment)
        }

        fun downloadLibrary() {
            // 测试一下利用三方库下载
            nav().navigateAction(R.id.action_demoFragment_to_downLoadLibraryFragment)
        }
    }

}
