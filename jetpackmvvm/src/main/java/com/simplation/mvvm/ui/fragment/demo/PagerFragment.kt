package com.simplation.mvvm.ui.fragment.demo

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.simplation.mvvm.R
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.databinding.FragmentPagerBinding
import com.simplation.mvvm.ui.fragment.collect.CollectFragment
import com.simplation.mvvm.ui.fragment.search.SearchFragment
import com.simplation.mvvm.ui.fragment.share.AriticleFragment
import com.simplation.mvvm.ui.fragment.todo.TodoListFragment
import com.simplation.mvvm.viewmodel.state.MainViewModel

/**
 * Pager fragment
 *      测试 Viewpager下的懒加载
 *
 * @constructor Create empty Pager fragment
 */
class PagerFragment : BaseFragment<MainViewModel, FragmentPagerBinding>() {

    private lateinit var pagerViewpager: ViewPager

    override fun layoutId(): Int = R.layout.fragment_pager

    override fun initView(savedInstanceState: Bundle?) {
        pagerViewpager = mDataBind.root.findViewById(R.id.pagerViewpager)
        pagerViewpager.adapter = object :
            FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> {
                        SearchFragment()
                    }
                    1 -> {
                        TodoListFragment()
                    }
                    2 -> {
                        AriticleFragment()
                    }
                    else -> {
                        CollectFragment()
                    }
                }
            }

            override fun getCount(): Int {
                return 5
            }
        }
        pagerViewpager.offscreenPageLimit = 5
    }

}
