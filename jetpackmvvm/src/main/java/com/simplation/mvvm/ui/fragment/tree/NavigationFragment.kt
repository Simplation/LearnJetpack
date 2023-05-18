package com.simplation.mvvm.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.*
import com.simplation.mvvm.app.weight.recyclerview.SpaceItemDecoration
import com.simplation.mvvm.databinding.IncludeListBinding
import com.simplation.mvvm.ui.adapter.NavigationAdapter
import com.simplation.mvvm.viewmodel.request.RequestTreeViewModel
import com.simplation.mvvm.viewmodel.state.TreeViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * @作者: Simplation
 * @日期: 2021/5/17 17:23
 * @描述: 导航
 * @更新:
 */


class NavigationFragment : BaseFragment<TreeViewModel, IncludeListBinding>() {
    // 界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: SwipeRecyclerView

    override fun layoutId(): Int = R.layout.include_list

    private val navigationAdapter: NavigationAdapter by lazy { NavigationAdapter(arrayListOf()) }

    private val requestTreeViewModel: RequestTreeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        // 状态页配置
        swipeRefreshLayout = mDataBind.root.findViewById(R.id.swipeRefresh)
        loadSir = loadServiceInit(swipeRefreshLayout) {
            // 点击重试时触发的操作
            loadSir.showLoading()
            requestTreeViewModel.getNavigationData()
        }
        // 初始化 RecyclerView
        recyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context), navigationAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFloatBtn(mDataBind.floatbtn)
        }
        // 初始化 SwipeRefreshLayout
        swipeRefreshLayout.init {
            // 触发刷新监听时请求数据
            requestTreeViewModel.getNavigationData()
        }
        navigationAdapter.setNavigationAction { item, _ ->
            nav().navigateAction(
                R.id.action_to_webFragment,
                Bundle().apply { putParcelable("ariticleData", item) })
        }
    }

    override fun lazyLoadData() {
        // 设置界面 加载中
        loadSir.showLoading()
        requestTreeViewModel.getNavigationData()
    }

    override fun createObserver() {
        requestTreeViewModel.navigationDataState.observe(viewLifecycleOwner, {
            swipeRefreshLayout.isRefreshing = false
            if (it.isSuccess) {
                loadSir.showSuccess()
                navigationAdapter.setList(it.listData)
            } else {
                loadSir.showError(it.errMessage)
            }
        })

        appViewModel.run {
            // 监听全局的主题颜色改变
            appColor.observe(this@NavigationFragment) {
                setUiTheme(it, mDataBind.floatbtn, swipeRefreshLayout, loadSir)
            }

            // 监听全局的列表动画改编
            appAnimation.observe(this@NavigationFragment) {
                navigationAdapter.setAdapterAnimation(it)
            }
        }
    }
}
