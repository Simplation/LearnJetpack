package com.simplation.mvvm.ui.fragment.collect

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.bindViewPager2
import com.simplation.mvvm.app.ext.init
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.databinding.FragmentCollectBinding
import com.simplation.mvvm.viewmodel.request.RequestCollectViewModel
import com.simplation.mvvmlib.ext.nav

/**
 * Collect fragment
 *
 * @constructor Create empty Collect fragment
 */
class CollectFragment : BaseFragment<RequestCollectViewModel, FragmentCollectBinding>() {

    private val titleData = arrayListOf("文章", "网址")

    private var fragments: ArrayList<Fragment> = arrayListOf()

    private lateinit var toolBar: Toolbar

    init {
        fragments.add(CollectAriticleFragment())
        fragments.add(CollectUrlFragment())
    }

    override fun layoutId(): Int = R.layout.fragment_collect

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        // 初始化时设置顶部主题颜色
        appViewModel.appColor.value?.let {
            mDataBind.collectViewpagerLinear.setBackgroundColor(it)
        }

        // 初始化 ViewPager2
        mDataBind.collectViewPager.init(this, fragments)

        // 初始化 MagicIndicator
        mDataBind.collectMagicIndicator.bindViewPager2(
            mDataBind.collectViewPager,
            mStringList = titleData
        )
        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.initClose { nav().navigateUp() }
    }

}
