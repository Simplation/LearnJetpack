package com.simplation.mvvm.ui.fragment.project

import android.os.Bundle
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
import com.simplation.mvvm.app.weight.recyclerview.DefineLoadMoreView
import com.simplation.mvvm.app.weight.recyclerview.SpaceItemDecoration
import com.simplation.mvvm.data.model.bean.CollectBus
import com.simplation.mvvm.databinding.IncludeListBinding
import com.simplation.mvvm.ui.adapter.AriticleAdapter
import com.simplation.mvvm.viewmodel.request.RequestCollectViewModel
import com.simplation.mvvm.viewmodel.request.RequestProjectViewModel
import com.simplation.mvvm.viewmodel.state.ProjectViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * Project child fragment
 *
 * @constructor Create empty Project child fragment
 */
class ProjectChildFragment : BaseFragment<ProjectViewModel, IncludeListBinding>() {

    // 适配器
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf()) }

    // 界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    // recyclerview 的底部加载 view 因为在首页要动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: SwipeRecyclerView

    // 是否是最新项目
    private var isNew = false

    // 改项目对应的 id
    private var cid = 0

    // 收藏 viewmodel
    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    // 请求的 ViewModel
    private val requestProjectViewModel: RequestProjectViewModel by viewModels()

    override fun layoutId(): Int = R.layout.include_list

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            isNew = it.getBoolean("isNew")
            cid = it.getInt("cid")
        }
        // 状态页配置
        swipeRefreshLayout = mDataBind.root.findViewById(R.id.swipeRefresh)
        loadsir = loadServiceInit(swipeRefreshLayout) {
            // 点击重试时触发的操作
            loadsir.showLoading()
            requestProjectViewModel.getProjectData(true, cid, isNew)
        }
        // 初始化 recyclerView
        recyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))

            footView = it.initFooter {
                // 触发加载更多时请求数据
                requestProjectViewModel.getProjectData(false, cid, isNew)
            }
            // 初始化 FloatingActionButton
            it.initFloatBtn(mDataBind.floatbtn)
        }
        // 初始化 SwipeRefreshLayout
        swipeRefreshLayout.init {
            // 触发刷新监听时请求数据
            requestProjectViewModel.getProjectData(true, cid, isNew)
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
                    putParcelable("ariticleData", articleAdapter.data[position])
                })
            }
            addChildClickViewIds(R.id.item_home_author, R.id.item_project_author)
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.item_home_author, R.id.item_project_author -> {
                        nav().navigateAction(
                            R.id.action_mainfragment_to_lookInfoFragment,
                            Bundle().apply {
                                putInt("id", articleAdapter.data[position].userId)
                            })
                    }
                }
            }
        }
    }

    override fun lazyLoadData() {
        loadsir.showLoading()
        requestProjectViewModel.getProjectData(true, cid, isNew)
    }

    override fun createObserver() {
        requestProjectViewModel.projectDataState.observe(viewLifecycleOwner, {
            // 设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, articleAdapter, loadsir, recyclerView, swipeRefreshLayout)
        })
        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
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
            userInfo.observe(this@ProjectChildFragment) {
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
            appColor.observe(this@ProjectChildFragment) {
                setUiTheme(it, mDataBind.floatbtn, swipeRefreshLayout, loadsir, footView)
            }
            // 监听全局的列表动画改编
            appAnimation.observe(this@ProjectChildFragment) {
                articleAdapter.setAdapterAnimation(it)
            }
            // 监听全局的收藏信息 收藏的 Id 跟本列表的数据 id 匹配则需要更新
            eventViewModel.collectEvent.observe(this@ProjectChildFragment) {
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

    companion object {
        fun newInstance(cid: Int, isNew: Boolean): ProjectChildFragment {
            val args = Bundle()
            args.putInt("cid", cid)
            args.putBoolean("isNew", isNew)
            val fragment = ProjectChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

}

