package com.simplation.mvvm.app.weight.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.simplation.mvvm.R
import com.simplation.mvvm.app.weight.recyclerview.GridDividerItemDecoration
import com.simplation.mvvm.data.model.enums.TodoType
import com.simplation.mvvm.ui.adapter.PriorityAdapter

/**
 * Priority dialog
 *
 * @constructor
 *
 * @param context
 * @param type
 */
class PriorityDialog(context: Context, type: Int) : Dialog(context, R.style.BottomDialogStyle) {
    private lateinit var shareAdapter: PriorityAdapter

    private var priorityInterface: PriorityInterface? = null
    private var proiorityData: ArrayList<TodoType> = ArrayList()

    var type = TodoType.TodoType1.type

    init {
        this.type = type
        // 拿到 Dialog 的 Window, 修改 Window 的属性
        val window = window
        window?.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            decorView.setPadding(0, 0, 0, 0)
            // 获取 Window 的 LayoutParams
            setCancelable(true)
            setCanceledOnTouchOutside(true)

            val attributes = attributes
            attributes.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                attributes.gravity = Gravity.BOTTOM
            }
            // 一定要重新设置, 才能生效
            window.attributes = attributes
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 添加数据
        TodoType.values().forEach {
            proiorityData.add(it)
        }

        // 初始化 Adapter
        shareAdapter = PriorityAdapter(proiorityData, type).apply {
            setOnItemClickListener { adapter, view, position ->
                priorityInterface?.run {
                    onSelect(proiorityData[position])
                }
                dismiss()
            }
        }

        // 初始化 RecyclerView
        val view = LayoutInflater.from(context).inflate(R.layout.todo_dialog, null)
        view.findViewById<RecyclerView>(R.id.tododialog_recycler).apply {
            context.let {
                layoutManager = GridLayoutManager(it, 3)
                setHasFixedSize(true)
                addItemDecoration(GridDividerItemDecoration(it, 0,  ConvertUtils.dp2px(24f), false))
                adapter = shareAdapter
            }
        }
    }

    fun setPriorityInterface(priorityInterface: PriorityInterface) {
        this.priorityInterface = priorityInterface
    }

    interface PriorityInterface {
        fun onSelect(type: TodoType)
    }
}