package com.simplation.mvvm.ui.fragment.lookinfo

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
import com.simplation.mvvm.app.weight.recyclerview.SpaceItemDecoration
import com.simplation.mvvm.data.model.bean.CollectBus
import com.simplation.mvvm.databinding.FragmentLookinfoBinding
import com.simplation.mvvm.ui.adapter.AriticleAdapter
import com.simplation.mvvm.viewmodel.request.RequestCollectViewModel
import com.simplation.mvvm.viewmodel.request.RequestLookInfoViewModel
import com.simplation.mvvm.viewmodel.state.LookInfoViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * Look info fragment
 *
 * @constructor Create empty Look info fragment
 */
class LookInfoFragment : BaseFragment<LookInfoViewModel, FragmentLookinfoBinding>() {
    // 界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf(), true) }

    // 请求的 ViewModel
    private val requestLookInfoViewModel: RequestLookInfoViewModel by viewModels()

    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var toolBar: Toolbar
    private lateinit var recyclerView: SwipeRecyclerView

    private var shareId = 0
    override fun layoutId(): Int = R.layout.fragment_lookinfo

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            shareId = it.getInt("id")
        }

        mDataBind.vm = mViewModel

        appViewModel.appColor.value?.let { mDataBind.shareLayout.setBackgroundColor(it) }

        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.initClose("他人信息") { nav().navigateUp() }

        swipeRefreshLayout = mDataBind.root.findViewById(R.id.swipeRefresh)
        loadSir = loadServiceInit(swipeRefreshLayout) {
            loadSir.showLoading()
            requestLookInfoViewModel.getLookInfo(shareId, true)
        }

        recyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                requestLookInfoViewModel.getLookInfo(shareId, false)
            }
            it.initFloatBtn(mDataBind.root.findViewById(R.id.floatbtn))
        }

        swipeRefreshLayout.init {
            requestLookInfoViewModel.getLookInfo(shareId, true)
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
                        articleAdapter.data[position - this@LookInfoFragment.recyclerView.headerCount]
                    )
                })
            }
        }
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        requestLookInfoViewModel.getLookInfo(shareId, true)
    }

    override fun createObserver() {
        requestLookInfoViewModel.shareResponse.observe(viewLifecycleOwner, {
            mViewModel.name.set(it.coinInfo.username)
            mViewModel.info.set("积分 : ${it.coinInfo.coinCount}　排名 : ${it.coinInfo.rank}")
        })
        requestLookInfoViewModel.shareListDataUiState.observe(viewLifecycleOwner, {
            // 设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, articleAdapter, loadSir, recyclerView, swipeRefreshLayout)
        })
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
            userInfo.observe(this@LookInfoFragment) {
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
            // 监听全局的收藏信息 收藏的 Id 跟本列表的数据 id 匹配则需要更新
            eventViewModel.collectEvent.observe(this@LookInfoFragment) {
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