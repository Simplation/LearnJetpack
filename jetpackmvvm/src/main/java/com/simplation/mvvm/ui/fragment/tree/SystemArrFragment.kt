package com.simplation.mvvm.ui.fragment.tree

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.bindViewPager2
import com.simplation.mvvm.app.ext.init
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.data.model.bean.SystemResponse
import com.simplation.mvvm.databinding.FragmentSystemBinding
import com.simplation.mvvm.viewmodel.state.TreeViewModel
import com.simplation.mvvmlib.ext.nav
import net.lucode.hackware.magicindicator.MagicIndicator

/**
 * System arr fragment
 *
 * @constructor Create empty System arr fragment
 */
class SystemArrFragment : BaseFragment<TreeViewModel, FragmentSystemBinding>() {

    lateinit var data: SystemResponse

    private lateinit var toolBar: Toolbar
    private lateinit var viewPager: ViewPager2
    private lateinit var frameLayout: FrameLayout
    private lateinit var magicIndicator: MagicIndicator

    private var index = 0

    private var fragments: ArrayList<Fragment> = arrayListOf()

    override fun layoutId() = R.layout.fragment_system

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            data = it.getParcelable("data")!!
            index = it.getInt("index")
        }

        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.initClose(data.name) {
            nav().navigateUp()
        }

        // 初始化时设置顶部主题颜色
        frameLayout = mDataBind.root.findViewById(R.id.viewpager_linear)
        appViewModel.appColor.value?.let { frameLayout.setBackgroundColor(it) }

        // 设置栏目标题居左显示
        magicIndicator = mDataBind.root.findViewById(R.id.magic_indicator)
        (magicIndicator.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.START

    }

    override fun lazyLoadData() {
        data.children.forEach {
            fragments.add(SystemChildFragment.newInstance(it.id))
        }

        // 初始化 viewpager2
        viewPager = mDataBind.root.findViewById(R.id.view_pager)
        viewPager.init(this, fragments)

        // 初始化 magic_indicator
        magicIndicator.bindViewPager2(viewPager, data.children.map { it.name })

        viewPager.offscreenPageLimit = fragments.size

        viewPager.postDelayed({
            viewPager.currentItem = index
        }, 100)

    }

    override fun createObserver() {

    }

}
