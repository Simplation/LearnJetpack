package com.simplation.mvvm.ui.fragment.todo

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.simplation.mvvm.R
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.eventViewModel
import com.simplation.mvvm.app.ext.*
import com.simplation.mvvm.app.weight.recyclerview.SpaceItemDecoration
import com.simplation.mvvm.databinding.FragmentListBinding
import com.simplation.mvvm.ui.adapter.TodoAdapter
import com.simplation.mvvm.viewmodel.request.RequestTodoViewModel
import com.simplation.mvvm.viewmodel.state.TodoViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * @作者: Simplation
 * @日期: 2021/4/26 9:57
 * @描述:
 * @更新:
 */

class TodoListFragment : BaseFragment<TodoViewModel, FragmentListBinding>() {

    private val todoAdapter: TodoAdapter by lazy { TodoAdapter(arrayListOf()) }

    // 界面管理者
    private lateinit var loadSir: LoadService<Any>

    private val requestTodoViewModel: RequestTodoViewModel by viewModels()

    private lateinit var toolBar: Toolbar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: SwipeRecyclerView

    override fun layoutId(): Int = R.layout.fragment_list

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.run {
            initClose("TODO") { nav().navigateUp() }
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_add -> {
                        nav().navigateAction(R.id.action_todoListFragment_to_addTodoFragment)
                    }
                }
                true
            }
        }

        swipeRefreshLayout = mDataBind.root.findViewById(R.id.swipeRefresh)
        loadSir = loadServiceInit(swipeRefreshLayout) {
            loadSir.showLoading()
            requestTodoViewModel.getTodoData(true)
        }

        recyclerView = mDataBind.root.findViewById(R.id.recyclerView)
        recyclerView.init(LinearLayoutManager(context), todoAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                requestTodoViewModel.getTodoData(false)
            }
            it.initFloatBtn(mDataBind.root.findViewById(R.id.floatbtn))
        }

        todoAdapter.run {
            setOnItemClickListener { _, _, position ->
                nav().navigateAction(
                    R.id.action_todoListFragment_to_addTodoFragment,
                    Bundle().apply {
                        putParcelable("todo", todoAdapter.data[position])
                    })
            }
            addChildClickViewIds(R.id.item_todo_setting)
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.item_todo_setting -> {
                        val items = if (todoAdapter.data[position].isDone()) {
                            listOf("删除", "编辑")
                        } else {
                            listOf("删除", "编辑", "完成")
                        }
                        activity?.let { activity ->
                            MaterialDialog(activity, BottomSheet())
                                .lifecycleOwner(viewLifecycleOwner).show {
                                    cornerRadius(8f)
                                    setPeekHeight(ConvertUtils.dp2px((items.size * 50 + 36).toFloat()))
                                    listItems(items = items) { _, index, _ ->
                                        when (index) {
                                            0 -> {
                                                // 删除
                                                requestTodoViewModel.delTodo(
                                                    todoAdapter.data[position].id,
                                                    position
                                                )
                                            }
                                            1 -> {
                                                // 编辑
                                                nav().navigateAction(R.id.action_todoListFragment_to_addTodoFragment,
                                                    Bundle().apply {
                                                        putParcelable(
                                                            "todo",
                                                            todoAdapter.data[position]
                                                        )
                                                    })
                                            }
                                            2 -> {
                                                // 完成
                                                requestTodoViewModel.doneTodo(
                                                    todoAdapter.data[position].id,
                                                    position
                                                )
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        }

    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        requestTodoViewModel.getTodoData(true)
    }

    override fun createObserver() {
        requestTodoViewModel.todoDataState.observe(viewLifecycleOwner, {
            // 设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, todoAdapter, loadSir, recyclerView, swipeRefreshLayout)
        })
        requestTodoViewModel.delDataState.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                if (todoAdapter.data.size == 1) {
                    loadSir.showEmpty()
                }
                // todoAdapter.remove(it.data!!)  // 过时的方法
                todoAdapter.removeAt(it.data!!)
            } else {
                showMessage(it.errorMsg)
            }
        })
        requestTodoViewModel.doneDataState.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                swipeRefreshLayout.isRefreshing = true
                requestTodoViewModel.getTodoData(true)
            } else {
                showMessage(it.errorMsg)
            }
        })

        eventViewModel.todoEvent.observe(this) {
            if (todoAdapter.data.size == 0) {
                // 界面没有数据时，变为加载中 增强一丢丢体验
                loadSir.showLoading()
            } else {
                // 有数据时，swipeRefresh 显示刷新状态
                swipeRefreshLayout.isRefreshing = true
            }
            // 请求数据
            requestTodoViewModel.getTodoData(true)
        }
    }

}
