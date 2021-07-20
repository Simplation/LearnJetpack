package com.simplation.mvvm.ui.fragment.search

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.eventViewModel
import com.simplation.mvvm.app.ext.*
import com.simplation.mvvm.app.weight.loadCallback.ErrorCallback
import com.simplation.mvvm.app.weight.recyclerview.SpaceItemDecoration
import com.simplation.mvvm.data.model.bean.CollectBus
import com.simplation.mvvm.databinding.FragmentListBinding
import com.simplation.mvvm.ui.adapter.AriticleAdapter
import com.simplation.mvvm.viewmodel.request.RequestCollectViewModel
import com.simplation.mvvm.viewmodel.request.RequestSearchViewModel
import com.simplation.mvvm.viewmodel.state.SearchViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.simplation.mvvmlib.ext.parseState
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * Search result fragment
 *
 * @constructor Create empty Search result fragment
 */
class SearchResultFragment : BaseFragment<SearchViewModel, FragmentListBinding>() {

    private var searchKey = ""

    private lateinit var loadSir: LoadService<Any>

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var toolBar: Toolbar
    private lateinit var swipeRecyclerView: SwipeRecyclerView

    private val ariticleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf()) }

    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    private val requestSearchViewModel: RequestSearchViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_list

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let { arguments -> arguments.getString("searchKey")?.let { searchKey = it } }

        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.initClose(searchKey) { nav().navigateUp() }

        swipeRefreshLayout = mDataBind.root.findViewById(R.id.swipeRefresh)
        // 配置状态页
        loadSir = loadServiceInit(swipeRefreshLayout) {
            loadSir.showLoading()
            requestSearchViewModel.getSearchResultData(searchKey, true)
        }

        // 初始化 RecyclerView
        swipeRecyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        swipeRecyclerView.init(LinearLayoutManager(context), ariticleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                requestSearchViewModel.getSearchResultData(searchKey, false)
            }
            it.initFloatBtn(mDataBind.root.findViewById(R.id.floatbtn))
        }

        swipeRefreshLayout.init {
            requestSearchViewModel.getSearchResultData(searchKey, true)
        }

        ariticleAdapter.run {
            setCollectClick { item, v, _ ->
                if (v.isChecked) {
                    requestCollectViewModel.uncollect(item.id)
                } else {
                    requestCollectViewModel.collect(item.id)
                }
            }

            setOnItemClickListener { _, _, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    putParcelable("ariticleData", ariticleAdapter.data[position])
                })
            }

            addChildClickViewIds(R.id.item_home_author, R.id.item_project_author)
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.item_home_author, R.id.item_project_author -> {
                        nav().navigateAction(
                            R.id.action_searchResultFragment_to_lookInfoFragment,
                            Bundle().apply {
                                putInt("id", ariticleAdapter.data[position].userId)
                            })
                    }
                }
            }
        }

    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        requestSearchViewModel.getSearchResultData(searchKey, true)
    }

    override fun createObserver() {
        requestSearchViewModel.seachResultData.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                swipeRefreshLayout.isRefreshing = false
                // 请求成功，页码 +1
                requestSearchViewModel.pageNo++
                if (it.isRefresh() && it.datas.size == 0) {
                    // 如果是第一页，并且没有数据，页面提示空布局
                    loadSir.showEmpty()
                } else if (it.isRefresh()) {
                    //如果是刷新的，有数据
                    loadSir.showSuccess()
                    ariticleAdapter.setList(it.datas)
                } else {
                    //不是第一页
                    loadSir.showSuccess()
                    ariticleAdapter.addData(it.datas)
                }
                swipeRecyclerView.loadMoreFinish(it.isEmpty(), it.hasMore())
            }, {
                // 这里代表请求失败
                swipeRefreshLayout.isRefreshing = false
                if (ariticleAdapter.data.size == 0) {
                    // 如果适配器数据没有值，则显示错误界面，并提示错误信息
                    loadSir.setErrorText(it.errMsg)
                    loadSir.showCallback(ErrorCallback::class.java)
                } else {
                    swipeRecyclerView.loadMoreError(0, it.errMsg)
                }
            })
        })
        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
            } else {
                showMessage(it.errorMsg)
                for (index in ariticleAdapter.data.indices) {
                    if (ariticleAdapter.data[index].id == it.id) {
                        ariticleAdapter.data[index].collect = it.collect
                        ariticleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        })
        appViewModel.run {
            // 监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
            userInfo.observe(this@SearchResultFragment) {
                if (it != null) {
                    it.collectIds.forEach { id ->
                        for (item in ariticleAdapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = true
                                break
                            }
                        }
                    }
                } else {
                    for (item in ariticleAdapter.data) {
                        item.collect = false
                    }
                }
                ariticleAdapter.notifyDataSetChanged()
            }

            // 监听全局的收藏信息 收藏的 Id 跟本列表的数据 id 匹配则需要更新
            eventViewModel.collectEvent.observe(this@SearchResultFragment) {
                for (index in ariticleAdapter.data.indices) {
                    if (ariticleAdapter.data[index].id == it.id) {
                        ariticleAdapter.data[index].collect = it.collect
                        ariticleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        }
    }
}
