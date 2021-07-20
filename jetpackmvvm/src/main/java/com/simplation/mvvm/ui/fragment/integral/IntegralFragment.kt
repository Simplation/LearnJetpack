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
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.*
import com.simplation.mvvm.app.weight.recyclerview.SpaceItemDecoration
import com.simplation.mvvm.data.model.bean.BannerResponse
import com.simplation.mvvm.data.model.bean.IntegralResponse
import com.simplation.mvvm.databinding.FragmentIntegralBinding
import com.simplation.mvvm.ui.adapter.IntegralAdapter
import com.simplation.mvvm.viewmodel.request.RequestIntegralViewModel
import com.simplation.mvvm.viewmodel.state.IntegralViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.simplation.mvvmlib.ext.view.gone
import com.simplation.mvvmlib.ext.view.notNull
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * Integral fragment
 *
 * @constructor Create empty Integral fragment
 */
class IntegralFragment : BaseFragment<IntegralViewModel, FragmentIntegralBinding>() {
    private var rank: IntegralResponse? = null

    // 界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    private lateinit var integralAdapter: IntegralAdapter

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var toolBar: Toolbar
    private lateinit var recyclerView: SwipeRecyclerView
    private lateinit var floatingactionbutton: FloatingActionButton

    private val requestIntegralViewModel: RequestIntegralViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_integral

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        mDataBind.vm = mViewModel

        rank = arguments?.getParcelable("rank")
        rank.notNull({
            mViewModel.rank.set(rank)
        }, {
            mDataBind.integralCardview.gone()
        })

        integralAdapter = IntegralAdapter(arrayListOf(), rank?.rank ?: -1)

        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.run {
            inflateMenu(R.menu.integral_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.integral_rules -> {
                        nav().navigateAction(R.id.action_to_webFragment,
                            Bundle().apply {
                                putParcelable(
                                    "bannerdata",
                                    BannerResponse(
                                        title = "积分规则",
                                        url = "https://www.wanandroid.com/blog/show/2653"
                                    )
                                )
                            }
                        )
                    }
                    R.id.integral_history -> {
                        nav().navigateAction(R.id.action_integralFragment_to_integralHistoryFragment)
                    }
                }
                true
            }
            initClose("积分排行") {
                nav().navigateUp()
            }
        }

        // 初始化界面管理器
        swipeRefreshLayout = mDataBind.root.findViewById(R.id.swipeRefresh)
        loadSir = loadServiceInit(swipeRefreshLayout) {
            loadSir.showLoading()
            requestIntegralViewModel.getIntegralData(true)
        }

        recyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context), integralAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                // 触发加载更多时请求数据
                requestIntegralViewModel.getIntegralData(false)
            }
            // 初始化 FloatingActionButton
            floatingactionbutton = mDataBind.root.findViewById(R.id.floatbtn)
            it.initFloatBtn(floatingactionbutton)
        }

        // 初始化 SwipeRefreshLayout
        swipeRefreshLayout.init {
            // 触发刷新监听时请求数据
            requestIntegralViewModel.getIntegralData(true)
        }

        appViewModel.appColor.value?.let {
            setUiTheme(
                it,
                mDataBind.integralMerank, mDataBind.integralMename, mDataBind.integralMecount
            )
        }
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        requestIntegralViewModel.getIntegralData(true)
    }

    override fun createObserver() {
        requestIntegralViewModel.integralDataState.observe(viewLifecycleOwner, {
            // 设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, integralAdapter, loadSir, recyclerView, swipeRefreshLayout)
        })
    }
}