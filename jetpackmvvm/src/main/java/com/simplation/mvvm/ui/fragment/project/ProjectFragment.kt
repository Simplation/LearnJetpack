package com.simplation.mvvm.ui.fragment.project

import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.kingja.loadsir.core.LoadService
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.*
import com.simplation.mvvm.app.weight.loadCallback.ErrorCallback
import com.simplation.mvvm.databinding.FragmentViewpagerBinding
import com.simplation.mvvm.viewmodel.request.RequestProjectViewModel
import com.simplation.mvvm.viewmodel.state.ProjectViewModel
import com.simplation.mvvmlib.ext.parseState
import net.lucode.hackware.magicindicator.MagicIndicator

/**
 * Project fragment
 *
 * @constructor Create empty Project fragment
 */
class ProjectFragment : BaseFragment<ProjectViewModel, FragmentViewpagerBinding>() {

    private lateinit var loadsir: LoadService<Any>

    private lateinit var viewpager: ViewPager2
    private lateinit var magicIndicator: MagicIndicator
    private lateinit var viewpagerLayout: FrameLayout

    // Fragment 集合
    private var fragments: ArrayList<Fragment> = arrayListOf()

    // 标题集合
    var mDataList: ArrayList<String> = arrayListOf()

    private val requestProjectViewModel: RequestProjectViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_viewpager

    override fun initView(savedInstanceState: Bundle?) {
        // 状态配置
        viewpager = mDataBind.root.findViewById(R.id.view_pager)
        loadsir = loadServiceInit(viewpager) {
            // 点击重试时触发的操作
            loadsir.showLoading()
            requestProjectViewModel.getProjectTitleData()
        }

        // 初始化 viewpager
        viewpager.init(this, fragments)

        // 初始化 magic_indicator
        magicIndicator = mDataBind.root.findViewById(R.id.magic_indicator)
        magicIndicator.bindViewPager2(viewpager, mDataList)
        appViewModel.appColor.value?.let {
            setUiTheme(
                it,
                mDataBind.root.findViewById(R.id.viewpager_linear),
                loadsir
            )
        }
    }

    override fun lazyLoadData() {
        // 显示正在加载...
        loadsir.showLoading()
        // 请求数据
        requestProjectViewModel.getProjectTitleData()
    }

    override fun createObserver() {
        requestProjectViewModel.titleData.observe(this, { data ->
            parseState(data, { it ->
                mDataList.clear()
                fragments.clear()
                mDataList.add("最新项目")
                mDataList.addAll(it.map { it.name })
                fragments.add(ProjectChildFragment.newInstance(0, true))
                it.forEach { classify ->
                    fragments.add(ProjectChildFragment.newInstance(classify.id, false))
                }
                magicIndicator.navigator.notifyDataSetChanged()
                viewpager.adapter?.notifyDataSetChanged()
                viewpager.offscreenPageLimit = fragments.size
                loadsir.showSuccess()
            }, {
                // 请求项目标题失败
                loadsir.showCallback(ErrorCallback::class.java)
                loadsir.setErrorText(it.errMsg)
            })
        })
        appViewModel.appColor.observe(this, Observer {
            setUiTheme(it, viewpagerLayout, loadsir)
        })
    }

}

