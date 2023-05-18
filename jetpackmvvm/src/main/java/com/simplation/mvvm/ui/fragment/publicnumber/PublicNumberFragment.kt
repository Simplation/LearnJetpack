package com.simplation.mvvm.ui.fragment.publicnumber

import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.kingja.loadsir.core.LoadService
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.*
import com.simplation.mvvm.app.weight.loadCallback.ErrorCallback
import com.simplation.mvvm.databinding.FragmentViewpagerBinding
import com.simplation.mvvm.viewmodel.request.RequestPublicNumberViewModel
import com.simplation.mvvmlib.ext.parseState
import net.lucode.hackware.magicindicator.MagicIndicator

/**
 * Public number fragment
 *
 * @constructor Create empty Public number fragment
 */
class PublicNumberFragment :
    BaseFragment<RequestPublicNumberViewModel, FragmentViewpagerBinding>() {
    // 界面状态管理者
    private lateinit var loadSir: LoadService<Any>
    private lateinit var viewPager: ViewPager2
    private lateinit var magicIndicator: MagicIndicator
    private lateinit var viewPagerLayout: FrameLayout

    // fragment 集合
    private val fragments: ArrayList<Fragment> = arrayListOf()

    // 标题集合
    private val mDataList: ArrayList<String> = arrayListOf()

    override fun layoutId(): Int = R.layout.fragment_viewpager

    override fun initView(savedInstanceState: Bundle?) {
        // 状态页配置
        viewPager = mDataBind.root.findViewById(R.id.view_pager)
        loadSir = loadServiceInit(viewPager) {
            // 点击重试时触发的操作
            loadSir.showLoading()
            mViewModel.getPublicTitleData()
        }

        // 初始化 viewpager2
        viewPager.init(this, fragments)
        // 初始化 magic_indicator
        magicIndicator = mDataBind.root.findViewById(R.id.magic_indicator)
        viewPagerLayout = mDataBind.root.findViewById(R.id.viewpager_linear)
        magicIndicator.bindViewPager2(viewPager, mDataList)
        appViewModel.appColor.value?.let { setUiTheme(it, viewPagerLayout, loadSir) }
    }

    override fun lazyLoadData() {
        // 设置界面 加载中
        loadSir.showLoading()
        // 加载并获取数据
        mViewModel.getPublicTitleData()
    }

    override fun createObserver() {
        mViewModel.titleData.observe(this) { data ->
            parseState(
                data,
                { it ->
                    mDataList.addAll(it.map { it.name })
                    it.forEach { classify ->
                        fragments.add(PublicChildFragment.newInstance(classify.id))
                    }
                    magicIndicator.navigator.notifyDataSetChanged()
                    viewPager.adapter?.notifyDataSetChanged()
                    viewPager.offscreenPageLimit = fragments.size
                    loadSir.showSuccess()
                }, {
                    // 请求项目标题失败
                    loadSir.showCallback(ErrorCallback::class.java)
                    loadSir.setErrorText(it.errMsg)
                }
            )
            appViewModel.appColor.observe(this) {
                setUiTheme(it, viewPagerLayout, loadSir)
            }
        }
    }

}
