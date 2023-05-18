package com.simplation.mvvm.ui.fragment.tree

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.bindViewPager2
import com.simplation.mvvm.app.ext.init
import com.simplation.mvvm.app.ext.setUiTheme
import com.simplation.mvvm.app.util.CacheUtil
import com.simplation.mvvm.databinding.FragmentViewpagerBinding
import com.simplation.mvvm.viewmodel.state.TreeViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import net.lucode.hackware.magicindicator.MagicIndicator

/**
 * Tree arr fragment
 *
 * @constructor Create empty Tree arr fragment
 */
class TreeArrFragment : BaseFragment<TreeViewModel, FragmentViewpagerBinding>() {

    private var titleData = arrayListOf("广场", "每日一问", "体系", "导航")

    private var fragments: ArrayList<Fragment> = arrayListOf()

    private lateinit var viewPagerLayout: FrameLayout
    private lateinit var includeViewpagerToolbar: Toolbar
    private lateinit var viewPager: ViewPager2
    private lateinit var magicIndicator: MagicIndicator

    init {
        fragments.add(PlazaFragment())
        fragments.add(AskFragment())
        fragments.add(SystemFragment())
        fragments.add(NavigationFragment())
    }

    override fun layoutId(): Int = R.layout.fragment_viewpager

    override fun initView(savedInstanceState: Bundle?) {
        appViewModel.appColor.value?.let {
            viewPagerLayout = mDataBind.root.findViewById(R.id.viewpager_linear)
            setUiTheme(it, viewPagerLayout)
        }

        includeViewpagerToolbar = mDataBind.root.findViewById(R.id.include_viewpager_toolbar)
        includeViewpagerToolbar.run {
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_add -> {
                        if (CacheUtil.isLogin()) {
                            nav().navigateAction(R.id.action_mainfragment_to_addAriticleFragment)
                        } else {
                            nav().navigateAction(R.id.action_to_loginFragment)
                        }
                    }
                }
                true
            }
        }
    }

    override fun lazyLoadData() {
        // 初始化 viewpager2
        viewPager = mDataBind.root.findViewById(R.id.view_pager)
        viewPager.init(this, fragments).offscreenPageLimit = fragments.size

        // 初始化 magic_indicator
        magicIndicator = mDataBind.root.findViewById(R.id.magic_indicator)
        magicIndicator.bindViewPager2(viewPager, mStringList = titleData) {
            if (it != 0) {
                includeViewpagerToolbar.menu.clear()
            } else {
                includeViewpagerToolbar.menu.hasVisibleItems().let { flag ->
                    if (!flag) includeViewpagerToolbar.inflateMenu(R.menu.todo_menu)
                }
            }
        }
    }

    override fun createObserver() {
        appViewModel.appColor.observe(this) {
            setUiTheme(it, mDataBind.root.findViewById(R.id.viewpager_linear))
        }
    }
}

