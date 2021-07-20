package com.simplation.mvvm.ui.fragment.share

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
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.eventViewModel
import com.simplation.mvvm.app.ext.*
import com.simplation.mvvm.app.weight.recyclerview.SpaceItemDecoration
import com.simplation.mvvm.databinding.FragmentListBinding
import com.simplation.mvvm.ui.adapter.ShareAdapter
import com.simplation.mvvm.viewmodel.request.RequestAriticleViewModel
import com.simplation.mvvm.viewmodel.state.ArticleViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView


/**
 * @作者: Simplation
 * @日期: 2021/4/26 10:00
 * @描述:
 * @更新:
 */


class AriticleFragment : BaseFragment<ArticleViewModel, FragmentListBinding>() {
    // 界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    private lateinit var toolBar: Toolbar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: SwipeRecyclerView

    private val articleAdapter: ShareAdapter by lazy { ShareAdapter(arrayListOf()) }

    private val requestViewModel: RequestAriticleViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_list

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.run {
            initClose("我分享的文章") { nav().navigateUp() }
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_add -> {
                        nav().navigateAction(R.id.action_ariticleFragment_to_addAriticleFragment)
                    }
                }
                true
            }
        }

        swipeRefreshLayout = mDataBind.root.findViewById(R.id.swipeRefresh)
        loadSir = loadServiceInit(swipeRefreshLayout) {
            loadSir.showLoading()
            requestViewModel.getShareData(true)
        }

        recyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                requestViewModel.getShareData(false)
            }
            it.initFloatBtn(mDataBind.root.findViewById(R.id.floatbtn))
        }

        swipeRefreshLayout.init {
            requestViewModel.getShareData(true)
        }

        articleAdapter.run {
            setOnItemClickListener { _, _, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    putParcelable("ariticleData", articleAdapter.data[position])
                })
            }
            addChildClickViewIds(R.id.item_share_del)
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.item_share_del -> {
                        showMessage("确定删除该文章吗？", positiveButtonText = "删除", positiveAction = {
                            requestViewModel.deleteShareData(
                                articleAdapter.data[position].id,
                                position
                            )
                        }, negativeButtonText = "取消")
                    }
                }
            }
        }
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        requestViewModel.getShareData(true)
    }

    override fun createObserver() {
        requestViewModel.shareDataState.observe(viewLifecycleOwner, {
            // 设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, articleAdapter, loadSir, recyclerView, swipeRefreshLayout)
        })
        requestViewModel.delDataState.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                // 删除成功 如果是删除的最后一个了，那么直接把界面设置为空布局
                if (articleAdapter.data.size == 1) {
                    loadSir.showEmpty()
                }
                articleAdapter.removeAt(it.data!!)  // 过时的方法：articleAdapter.remove(it.data!!)
            } else {
                // 删除失败，提示
                showMessage(it.errorMsg)
            }
        })
        eventViewModel.shareArticleEvent.observe(this) {
            if (articleAdapter.data.size == 0) {
                // 界面没有数据时，变为加载中 增强一丢丢体验
                loadSir.showLoading()
            } else {
                // 有数据时，swipeRefresh 显示刷新状态
                swipeRefreshLayout.isRefreshing = true
            }
            requestViewModel.getShareData(true)
        }
    }
}