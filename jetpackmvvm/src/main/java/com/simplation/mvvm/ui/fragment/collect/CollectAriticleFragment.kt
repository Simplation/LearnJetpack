package com.simplation.mvvm.ui.fragment.collect

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.simplation.mvvm.R
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.eventViewModel
import com.simplation.mvvm.app.ext.*
import com.simplation.mvvm.app.weight.recyclerview.SpaceItemDecoration
import com.simplation.mvvm.data.model.bean.CollectBus
import com.simplation.mvvm.databinding.IncludeListBinding
import com.simplation.mvvm.ui.adapter.CollectAdapter
import com.simplation.mvvm.viewmodel.request.RequestCollectViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * Collect ariticle fragment
 *      收藏的文章的 Fragment
 *
 * @constructor Create empty Collect ariticle fragment
 */
class CollectAriticleFragment : BaseFragment<RequestCollectViewModel, IncludeListBinding>() {

    private lateinit var loadSir: LoadService<Any>

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: SwipeRecyclerView

    private val articleAdapter: CollectAdapter by lazy { CollectAdapter(arrayListOf()) }

    override fun layoutId(): Int = R.layout.include_list

    override fun initView(savedInstanceState: Bundle?) {
        swipeRefreshLayout = mDataBind.root.findViewById(R.id.swipeRefresh)
        loadSir = loadServiceInit(swipeRefreshLayout) {
            loadSir.showLoading()
            mViewModel.getCollectAriticleData(true)
        }

        // 初始化 RecyclerView
        recyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                mViewModel.getCollectAriticleData(false)
            }
            // 初始化 FloatingActionButton
            it.initFloatBtn(mDataBind.floatbtn)
        }

        // 初始化 SwipeRefreshLayout
        swipeRefreshLayout.init {
            mViewModel.getCollectAriticleData(true)
        }

        articleAdapter.run {
            setCollectClick { item, v, _ ->
                if (v.isChecked) {
                    mViewModel.uncollect(item.originId)
                } else {
                    mViewModel.collect(item.originId)
                }
            }
            setOnItemClickListener { _, _, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    putParcelable("collect", articleAdapter.data[position])
                })
            }
        }
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        mViewModel.getCollectAriticleData(true)
    }

    override fun createObserver() {
        mViewModel.ariticleDataState.observe(viewLifecycleOwner, {
            // 设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, articleAdapter, loadSir, recyclerView,swipeRefreshLayout)
        })

        mViewModel.collectUiState.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                // 收藏或取消收藏操作成功，发送全局收藏消息
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
            } else {
                showMessage(it.errorMsg)
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].originId == it.id) {
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        })

        eventViewModel.run {
            // 监听全局的收藏信息 收藏的 Id 跟本列表的数据 id 匹配则 需要删除他 否则则请求最新收藏数据
            collectEvent.observe(this@CollectAriticleFragment, Observer {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].originId == it.id) {
                        articleAdapter.removeAt(index)
                        if (articleAdapter.data.size == 0) {
                            loadSir.showEmpty()
                        }
                        return@Observer
                    }
                }
                mViewModel.getCollectAriticleData(true)
            })
        }
    }
}