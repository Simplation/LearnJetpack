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
import com.simplation.mvvm.databinding.IncludeListBinding
import com.simplation.mvvm.ui.adapter.CollectUrlAdapter
import com.simplation.mvvm.viewmodel.request.RequestCollectViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView


/**
 * Collect url fragment
 *
 * @constructor Create empty Collect url fragment
 */
class CollectUrlFragment : BaseFragment<RequestCollectViewModel, IncludeListBinding>() {

    private lateinit var loadSir: LoadService<Any>

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: SwipeRecyclerView

    private val articleAdapter: CollectUrlAdapter by lazy { CollectUrlAdapter(arrayListOf()) }

    override fun layoutId(): Int = R.layout.include_list

    override fun initView(savedInstanceState: Bundle?) {
        // 初始化 LoadService
        swipeRefreshLayout = mDataBind.root.findViewById(R.id.swipeRefresh)
        loadSir = loadServiceInit(swipeRefreshLayout) {
            loadSir.showLoading()
            mViewModel.getCollectUrlData()
        }

        // 初始化 RecyclerView
        recyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            // 初始化 FloatActionButton
            it.initFloatBtn(mDataBind.floatbtn)
        }

        // 初始化 SwipeRefreshLayout
        swipeRefreshLayout.init {
            mViewModel.getCollectUrlData()
        }

        articleAdapter.run {
            setCollectClick { item, v, _ ->
                if (v.isChecked) {
                    mViewModel.uncollectUrl(item.id)
                } else {
                    mViewModel.collectUrl(item.name, item.link)
                }
            }
            setOnItemClickListener { _, _, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    putParcelable("collectUrl", articleAdapter.data[position])
                })
            }
        }
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        mViewModel.getCollectUrlData()
    }

    override fun createObserver() {
        mViewModel.urlDataState.observe(viewLifecycleOwner, {
            swipeRefreshLayout.isRefreshing = false
            recyclerView.loadMoreFinish(it.isEmpty, it.hasMore)
            if (it.isSuccess) {
                // 成功
                when {
                    // 第一页并没有数据 显示空布局界面
                    it.isEmpty -> {
                        loadSir.showEmpty()
                    }
                    else -> {
                        loadSir.showSuccess()
                        articleAdapter.setList(it.listData)
                    }
                }
            } else {
                // 失败
                loadSir.showError(it.errMessage)
            }
        })
        mViewModel.collectUrlUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.removeAt(index)
                        if (articleAdapter.data.size == 0) {
                            loadSir.showEmpty()
                        }
                        return@Observer
                    }
                }
            } else {
                showMessage(it.errorMsg)
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        })

        eventViewModel.run {
            // 监听全局的收藏信息 收藏的 Id 跟本列表的数据 id 匹配则 需要删除他 否则则请求最新收藏数据
            collectEvent.observe(this@CollectUrlFragment, Observer {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data.removeAt(index)
                        articleAdapter.notifyItemChanged(index)
                        if (articleAdapter.data.size == 0) {
                            loadSir.showEmpty()
                        }
                        return@Observer
                    }
                }
                mViewModel.getCollectUrlData()
            })
        }
    }
}