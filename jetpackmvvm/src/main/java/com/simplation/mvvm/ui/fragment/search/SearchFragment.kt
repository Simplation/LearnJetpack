package com.simplation.mvvm.ui.fragment.search

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.init
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.app.ext.setUiTheme
import com.simplation.mvvm.app.util.CacheUtil
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.databinding.FragmentSearchBinding
import com.simplation.mvvm.ui.adapter.SearcHistoryAdapter
import com.simplation.mvvm.ui.adapter.SearcHotAdapter
import com.simplation.mvvm.viewmodel.request.RequestSearchViewModel
import com.simplation.mvvm.viewmodel.state.SearchViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.simplation.mvvmlib.ext.parseState
import com.simplation.mvvmlib.ext.util.toJson

/**
 * Search fragment
 *
 * @constructor Create empty Search fragment
 */
class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {

    private val historyAdapter: SearcHistoryAdapter by lazy { SearcHistoryAdapter(arrayListOf()) }

    private val hotAdapter: SearcHotAdapter by lazy { SearcHotAdapter(arrayListOf()) }

    private val requestSearchViewModel: RequestSearchViewModel by viewModels()

    private lateinit var toolBar: Toolbar

    override fun layoutId(): Int = R.layout.fragment_search

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        setMenu()

        appViewModel.appColor.value?.let {
            setUiTheme(
                it,
                mDataBind.searchText1,
                mDataBind.searchText2
            )
        }

        // 初始化历史搜索的 RecyclerView
        mDataBind.searchHistoryRv.init(LinearLayoutManager(context), historyAdapter, false)

        // 初始化热门搜索的 RecyclerView
        val layoutManager = FlexboxLayoutManager(context)
        // 方向 主轴为水平方向，起点在左端
        layoutManager.flexDirection = FlexDirection.ROW
        // 左对齐
        layoutManager.justifyContent = JustifyContent.FLEX_START
        mDataBind.searchHotRv.init(layoutManager, hotAdapter, false)

        historyAdapter.run {
            setOnItemClickListener { _, _, position ->
                val queryStr = historyAdapter.data[position]
                updateKey(queryStr)
                nav().navigateAction(R.id.action_searchFragment_to_searchResultFragment,
                    Bundle().apply {
                        putString("searchKey", queryStr)
                    }
                )
            }
            addChildClickViewIds(R.id.item_history_del)
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.item_history_del -> {
                        requestSearchViewModel.historyData.value?.let {
                            it.removeAt(position)
                            requestSearchViewModel.historyData.value= it
                        }
                    }
                }
            }
        }

        hotAdapter.run {
            setOnItemClickListener { _, _, position ->
                val queryStr = hotAdapter.data[position].name
                updateKey(queryStr)
                nav().navigateAction(R.id.action_searchFragment_to_searchResultFragment,
                    Bundle().apply {
                        putString("searchKey", queryStr)
                    }
                )
            }
        }

        mDataBind.searchClear.setOnClickListener {
            activity?.let {
                MaterialDialog(it)
                    .cancelable(false)
                    .lifecycleOwner(this)
                    .show {
                        title(text = "温馨提示")
                        message(text = "确定清空吗?")
                        negativeButton(text = "取消")
                        positiveButton(text = "清空") {
                            //清空
                            requestSearchViewModel.historyData.value = arrayListOf()
                        }
                        getActionButton(WhichButton.POSITIVE).updateTextColor(
                            SettingUtil.getColor(
                                it
                            )
                        )
                        getActionButton(WhichButton.NEGATIVE).updateTextColor(
                            SettingUtil.getColor(
                                it
                            )
                        )
                    }
            }
        }
    }

    override fun createObserver() {
        requestSearchViewModel.run {
            // 监听热门数据变化
            hotData.observe(viewLifecycleOwner, { resultState ->
                parseState(resultState, {
                    hotAdapter.setList(it)
                })
            })
            // 监听历史数据变化
            historyData.observe(viewLifecycleOwner, {
                historyAdapter.data = it
                historyAdapter.notifyDataSetChanged()
                CacheUtil.setSearchHistoryData(it.toJson())
            })
        }
    }

    override fun lazyLoadData() {
        // 获取历史搜索词数据
        requestSearchViewModel.getHistoryData()
        // 获取热门数据
        requestSearchViewModel.getHotData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.run {
            maxWidth = Integer.MAX_VALUE
            onActionViewExpanded()
            queryHint = "输入关键字搜索"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                // searchview 的监听
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // 当点击搜索时 输入法的搜索，和右边的搜索都会触发
                    query?.let { queryStr ->
                        updateKey(queryStr)
                        nav().navigateAction(R.id.action_searchFragment_to_searchResultFragment,
                            Bundle().apply {
                                putString("searchKey", queryStr)
                            }
                        )
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            isSubmitButtonEnabled = true // 右边是否展示搜索图标
            val field = javaClass.getDeclaredField("mGoButton")
            field.run {
                isAccessible = true
                val mGoButton = get(searchView) as ImageView
                mGoButton.setImageResource(R.mipmap.ic_search)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    /**
     * 更新搜索词
     */
    fun updateKey(keyStr: String) {
        requestSearchViewModel.historyData.value?.let {
            if (it.contains(keyStr)) {
                // 当搜索历史中包含该数据时 删除
                it.remove(keyStr)
            } else if (it.size >= 10) {
                // 如果集合的 size 有 10 个以上了，删除最后一个
                it.removeAt(it.size - 1)
            }
            // 添加新数据到第一条
            it.add(0, keyStr)
            requestSearchViewModel.historyData.value = it
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        // 当该 Fragment 重新获得视图时，重新设置 Menu，防止退出 WebFragment ActionBar 被清空后，导致该界面的 ActionBar 无法显示 bug
        if (!hidden) {
            setMenu()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setMenu() {
        setHasOptionsMenu(true)
        toolBar.run {
            // 设置 menu 关键代码
            mActivity.setSupportActionBar(this)
            initClose { nav().navigateUp() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivity.setSupportActionBar(null)
    }

}
