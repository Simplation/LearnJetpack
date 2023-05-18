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
import com.simplation.mvvm.data.model.bean.SystemResponse
import com.simplation.mvvm.databinding.IncludeListBinding
import com.simplation.mvvm.ui.adapter.SystemAdapter
import com.simplation.mvvm.viewmodel.request.RequestTreeViewModel
import com.simplation.mvvm.viewmodel.state.TreeViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * System fragment
 *
 * @constructor Create empty System fragment
 */
class SystemFragment : BaseFragment<TreeViewModel, IncludeListBinding>() {

    // 界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: SwipeRecyclerView

    override fun layoutId() = R.layout.include_list

    private val systemAdapter: SystemAdapter by lazy { SystemAdapter(arrayListOf()) }

    private val requestTreeViewModel: RequestTreeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        // 状态页配置
        swipeRefreshLayout = mDataBind.root.findViewById(R.id.swipeRefresh)
        loadsir = loadServiceInit(swipeRefreshLayout) {
            // 点击重试时触发的操作
            loadsir.showLoading()
            requestTreeViewModel.getSystemData()
        }
        // 初始化 recyclerView
        recyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context), systemAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFloatBtn(mDataBind.floatbtn)
        }
        // 初始化 SwipeRefreshLayout
        swipeRefreshLayout.init {
            // 触发刷新监听时请求数据
            requestTreeViewModel.getSystemData()
        }
        systemAdapter.run {
            setOnItemClickListener { _, _, position ->
                if (systemAdapter.data[position].children.isNotEmpty()) {
                    nav().navigateAction(R.id.action_mainfragment_to_systemArrFragment,
                        Bundle().apply {
                            putParcelable("data", systemAdapter.data[position])
                        }
                    )
                }
            }
            setChildClick { item: SystemResponse, _, position ->
                nav().navigateAction(R.id.action_mainfragment_to_systemArrFragment,
                    Bundle().apply {
                        putParcelable("data", item)
                        putInt("index", position)
                    }
                )
            }
        }
    }

    override fun lazyLoadData() {
        // 设置界面 加载中
        loadsir.showLoading()
        requestTreeViewModel.getSystemData()
    }

    override fun createObserver() {
        requestTreeViewModel.systemDataState.observe(viewLifecycleOwner, {
            swipeRefreshLayout.isRefreshing = false
            if (it.isSuccess) {
                loadsir.showSuccess()
                systemAdapter.setList(it.listData)
            } else {
                loadsir.showError(it.errMessage)
            }
        })

        appViewModel.run {
            // 监听全局的主题颜色改变
            appColor.observe(this@SystemFragment) {
                setUiTheme(it, mDataBind.floatbtn, swipeRefreshLayout, loadsir)
            }
            // 监听全局的列表动画改编
            appAnimation.observe(this@SystemFragment) {
                systemAdapter.setAdapterAnimation(it)
            }
        }

    }
}
