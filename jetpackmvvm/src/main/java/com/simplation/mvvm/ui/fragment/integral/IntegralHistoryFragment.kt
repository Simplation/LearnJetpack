package com.simplation.mvvm.ui.fragment.integral

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ConvertUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kingja.loadsir.core.LoadService
import com.simplation.mvvm.R
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.*
import com.simplation.mvvm.app.weight.recyclerview.SpaceItemDecoration
import com.simplation.mvvm.databinding.FragmentListBinding
import com.simplation.mvvm.ui.adapter.IntegralHistoryAdapter
import com.simplation.mvvm.viewmodel.request.RequestIntegralViewModel
import com.simplation.mvvm.viewmodel.state.IntegralViewModel
import com.simplation.mvvmlib.ext.nav
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * Integral history fragment
 *
 * @constructor Create empty Integral history fragment
 */
class IntegralHistoryFragment : BaseFragment<IntegralViewModel, FragmentListBinding>() {
    // 界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    // 适配器
    private val integralAdapter: IntegralHistoryAdapter by lazy { IntegralHistoryAdapter(arrayListOf()) }

    // 请求的 ViewModel
    private val requestIntegralViewModel: RequestIntegralViewModel by viewModels()

    private lateinit var toolBar: Toolbar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: SwipeRecyclerView
    private lateinit var floatBtn: FloatingActionButton
    override fun layoutId(): Int = R.layout.fragment_list

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.initClose("积分记录") { nav().navigateUp() }

        // 状态页配置
        swipeRefreshLayout = mDataBind.root.findViewById(R.id.swipeRefresh)
        loadSir = loadServiceInit(swipeRefreshLayout) {
            loadSir.showLoading()
            requestIntegralViewModel.getIntegralHistoryData(true)
        }

        // 初始化 RecyclerView
        recyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context), integralAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                requestIntegralViewModel.getIntegralHistoryData(false)
            }
            // 初始化 FloatActionButton
            floatBtn = mDataBind.root.findViewById(R.id.floatbtn)
            it.initFloatBtn(floatBtn)
        }

        // 初始化 SwipeRefreshLayout
        swipeRefreshLayout.init {
            // 触发刷新监听时请求数据
            requestIntegralViewModel.getIntegralHistoryData(true)
        }
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        requestIntegralViewModel.getIntegralHistoryData(true)
    }

    override fun createObserver() {
        requestIntegralViewModel.integralHistoryDataState.observe(viewLifecycleOwner, {
            loadListData(it, integralAdapter, loadSir, recyclerView, swipeRefreshLayout)
        })
    }

}