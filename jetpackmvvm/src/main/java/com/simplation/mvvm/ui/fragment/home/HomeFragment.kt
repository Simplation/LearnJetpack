package com.simplation.mvvm.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
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
import com.simplation.mvvm.app.eventViewModel
import com.simplation.mvvm.app.ext.*
import com.simplation.mvvm.app.weight.banner.HomeBannerAdapter
import com.simplation.mvvm.app.weight.banner.HomeBannerViewHolder
import com.simplation.mvvm.app.weight.recyclerview.DefineLoadMoreView
import com.simplation.mvvm.app.weight.recyclerview.SpaceItemDecoration
import com.simplation.mvvm.data.model.bean.BannerResponse
import com.simplation.mvvm.data.model.bean.CollectBus
import com.simplation.mvvm.databinding.FragmentHomeBinding
import com.simplation.mvvm.ui.adapter.AriticleAdapter
import com.simplation.mvvm.viewmodel.request.RequestCollectViewModel
import com.simplation.mvvm.viewmodel.request.RequestHomeViewModel
import com.simplation.mvvm.viewmodel.state.HomeViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.simplation.mvvmlib.ext.parseState
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.zhpan.bannerview.BannerViewPager

/**
 * Home fragment
 *
 * @constructor Create empty Home fragment
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    // 适配器
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf(), true) }

    // 界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    // recyclerview 的底部加载 view 因为在首页要动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    // 收藏 viewModel
    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    // 请求数据 ViewModel
    private val requestHomeViewModel: RequestHomeViewModel by viewModels()

    private lateinit var toolbar: Toolbar

    private lateinit var recyclerView: SwipeRecyclerView

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var floatbtn: FloatingActionButton

    override fun layoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        swipeRefresh = mDataBind.root.findViewById(R.id.swipeRefresh)
        // 状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            // 点击重试时触发的操作
            loadsir.showLoading()
            requestHomeViewModel.getBannerData()
            requestHomeViewModel.getHomeData(true)
        }

        // 初始化 toolbar
        toolbar = mDataBind.root.findViewById(R.id.toolbar)
        toolbar.run {
            init("首页")
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        nav().navigateAction(R.id.action_mainfragment_to_searchFragment)
                    }
                }
                true
            }
        }
        // 初始化 recyclerView
        recyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            // 因为首页要添加轮播图，所以我设置了 firstNeedTop 字段为 false,即第一条数据不需要设置间距
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))
            footView = it.initFooter {
                requestHomeViewModel.getHomeData(false)
            }
            floatbtn = mDataBind.root.findViewById(R.id.floatbtn)
            // 初始化 FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        // 初始化 SwipeRefreshLayout
        swipeRefresh.init {
            // 触发刷新监听时请求数据
            requestHomeViewModel.getHomeData(true)
        }
        articleAdapter.run {
            setCollectClick { item, v, _ ->
                if (v.isChecked) {
                    requestCollectViewModel.uncollect(item.id)
                } else {
                    requestCollectViewModel.collect(item.id)
                }
            }
            setOnItemClickListener { _, _, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    putParcelable(
                        "ariticleData",
                        articleAdapter.data[position - this@HomeFragment.recyclerView.headerCount]
                    )
                })
            }
            addChildClickViewIds(R.id.item_home_author, R.id.item_project_author)
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.item_home_author, R.id.item_project_author -> {
                        nav().navigateAction(
                            R.id.action_mainfragment_to_lookInfoFragment,
                            Bundle().apply {
                                putInt(
                                    "id",
                                    articleAdapter.data[position - this@HomeFragment.recyclerView.headerCount].userId
                                )
                            })
                    }
                }
            }
        }
    }

    /**
     * 懒加载
     */
    override fun lazyLoadData() {
        // 设置界面 加载中
        loadsir.showLoading()
        // 请求轮播图数据
        requestHomeViewModel.getBannerData()
        // 请求文章列表数据
        requestHomeViewModel.getHomeData(true)
    }

    override fun createObserver() {
        requestHomeViewModel.run {
            // 监听首页文章列表请求的数据变化
            homeDataState.observe(viewLifecycleOwner, {
                // 设值 新写了个拓展函数，搞死了这个恶心的重复代码
                loadListData(it, articleAdapter, loadsir, recyclerView, swipeRefresh)
            })

            // 监听轮播图请求的数据变化
            bannerData.observe(viewLifecycleOwner, { resultState ->
                parseState(resultState, { data ->
                    // 请求轮播图数据成功，添加轮播图到 headview ，如果等于0说明没有添加过头部，添加一个
                    if (recyclerView.headerCount == 0) {
                        val headview =
                            LayoutInflater.from(context).inflate(R.layout.include_banner, null)
                                .apply {
                                    findViewById<BannerViewPager<BannerResponse, HomeBannerViewHolder>>(
                                        R.id.banner_view
                                    ).apply {
                                        adapter = HomeBannerAdapter()
                                        setLifecycleRegistry(lifecycle)
                                        setOnPageClickListener {
                                            nav().navigateAction(
                                                R.id.action_to_webFragment,
                                                Bundle().apply {
                                                    putParcelable("bannerdata", data[it])
                                                })
                                        }
                                        create(data)
                                    }
                                }
                        recyclerView.addHeaderView(headview)
                        recyclerView.scrollToPosition(0)
                    }
                })
            })
        }
        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                // 收藏或取消收藏操作成功，发送全局收藏消息
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
            } else {
                showMessage(it.errorMsg)
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data[index].collect = it.collect
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        })

        appViewModel.run {
            // 监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
            userInfo.observe(this@HomeFragment) {
                if (it != null) {
                    it.collectIds.forEach { id ->
                        for (item in articleAdapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = true
                                break
                            }
                        }
                    }
                } else {
                    for (item in articleAdapter.data) {
                        item.collect = false
                    }
                }
                articleAdapter.notifyDataSetChanged()
            }
            // 监听全局的主题颜色改变
            appColor.observe(this@HomeFragment) {
                setUiTheme(it, toolbar, floatbtn, swipeRefresh, loadsir, footView)
            }
            // 监听全局的列表动画改编
            appAnimation.observe(this@HomeFragment) {
                articleAdapter.setAdapterAnimation(it)
            }
            // 监听全局的收藏信息 收藏的 Id 跟本列表的数据 id 匹配则需要更新
            eventViewModel.collectEvent.observe(this@HomeFragment) {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data[index].collect = it.collect
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        }
    }

}

