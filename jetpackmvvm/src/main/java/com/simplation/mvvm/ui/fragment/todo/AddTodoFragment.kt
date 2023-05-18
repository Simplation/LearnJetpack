package com.simplation.mvvm.ui.fragment.todo

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.eventViewModel
import com.simplation.mvvm.app.ext.initClose
import com.simplation.mvvm.app.ext.showMessage
import com.simplation.mvvm.app.util.DatetimeUtil
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.app.weight.custom.PriorityDialog
import com.simplation.mvvm.data.model.bean.TodoResponse
import com.simplation.mvvm.data.model.enums.TodoType
import com.simplation.mvvm.databinding.FragmentAddtodoBinding
import com.simplation.mvvm.viewmodel.request.RequestTodoViewModel
import com.simplation.mvvm.viewmodel.state.TodoViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.util.notNull
import java.util.*

/**
 * Add todo fragment
 *
 * @constructor Create empty Add todo fragment
 */
class AddTodoFragment : BaseFragment<TodoViewModel, FragmentAddtodoBinding>() {

    private var todoResponse: TodoResponse? = null

    private val requestTodoViewModel: RequestTodoViewModel by viewModels()

    private lateinit var toolBar: Toolbar

    override fun layoutId(): Int = R.layout.fragment_addtodo

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(savedInstanceState: Bundle?) {
        mDataBind.vm = mViewModel
        mDataBind.click = ProxyClick()
        arguments?.let {
            todoResponse = it.getParcelable("todo")
            todoResponse?.let { todo ->
                mViewModel.todoTitle.set(todo.title)
                mViewModel.todoContent.set(todo.content)
                mViewModel.todoTime.set(todo.dateStr)
                mViewModel.todoLeve.set(TodoType.byType(todo.priority).content)
                mViewModel.todoColor.set(TodoType.byType(todo.priority).color)
            }
        }

        toolBar = mDataBind.root.findViewById(R.id.toolbar)
        toolBar.initClose(if (todoResponse == null) "添加TODO" else "修改TODO") {
            nav().navigateUp()
        }
        appViewModel.appColor.value?.let { SettingUtil.setShapeColor(mDataBind.addtodoSubmit, it) }
    }

    override fun createObserver() {
        requestTodoViewModel.updateDataState.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                // 添加TODO 成功 返回并发送消息回调
                nav().navigateUp()
                eventViewModel.todoEvent.setValue(false)
            } else {
                showMessage(it.errorMsg)
            }
        })
    }

    inner class ProxyClick {
        // 选择时间
        fun todoTime() {
            activity?.let {
                MaterialDialog(it)
                    .lifecycleOwner(this@AddTodoFragment).show {
                        cornerRadius(0f)
                        datePicker(minDate = Calendar.getInstance()) { _, date ->
                            mViewModel.todoTime.set(
                                DatetimeUtil.formatDate(
                                    date.time,
                                    DatetimeUtil.DATE_PATTERN
                                )
                            )
                        }
                    }
            }
        }

        // 选择类型
        fun todoType() {
            activity?.let {
                PriorityDialog(it, TodoType.byValue(mViewModel.todoLeve.get()).type).apply {
                    setPriorityInterface(object : PriorityDialog.PriorityInterface {
                        override fun onSelect(type: TodoType) {
                            mViewModel.todoLeve.set(type.content)
                            mViewModel.todoColor.set(type.color)
                        }
                    })
                }.show()
            }
        }

        // 提交
        fun submit() {
            when {
                mViewModel.todoTitle.get().isEmpty() -> {
                    showMessage("请填写标题")
                }
                mViewModel.todoContent.get().isEmpty() -> {
                    showMessage("请填写内容")
                }
                mViewModel.todoTime.get().isEmpty() -> {
                    showMessage("请选择预计完成时间")
                }
                else -> {
                    todoResponse.notNull({
                        showMessage("确认提交编辑吗？", positiveButtonText = "提交", positiveAction = {
                            requestTodoViewModel.updateTodo(
                                it.id,
                                mViewModel.todoTitle.get(),
                                mViewModel.todoContent.get(),
                                mViewModel.todoTime.get(),
                                TodoType.byValue(mViewModel.todoLeve.get()).type
                            )
                        }, negativeButtonText = "取消")
                    }, {
                        showMessage("确认添加吗？", positiveButtonText = "添加", positiveAction = {
                            requestTodoViewModel.addTodo(
                                mViewModel.todoTitle.get(),
                                mViewModel.todoContent.get(),
                                mViewModel.todoTime.get(),
                                TodoType.byValue(mViewModel.todoLeve.get()).type
                            )
                        }, negativeButtonText = "取消")
                    })
                }
            }
        }
    }

}