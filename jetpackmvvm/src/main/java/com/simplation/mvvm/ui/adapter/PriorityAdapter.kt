package com.simplation.mvvm.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.simplation.mvvm.R
import com.simplation.mvvm.app.weight.preference.MyColorCircleView
import com.simplation.mvvm.data.model.enums.TodoType

/**
 * Priority adapter
 *
 * @constructor
 *
 * @param data
 */
class PriorityAdapter(data: ArrayList<TodoType>) :
    BaseQuickAdapter<TodoType, BaseViewHolder>(R.layout.item_todo_dialog, data) {

    var checkType = TodoType.TodoType1.type

    constructor(data: ArrayList<TodoType>, checkType: Int) : this(data) {
        this.checkType = checkType
    }

    override fun convert(holder: BaseViewHolder, item: TodoType) {
        // 进行赋值操作
        item.run {
            holder.setText(R.id.item_todo_dialog_name, item.content)
            if (checkType == item.type) {
                holder.getView<MyColorCircleView>(R.id.item_todo_dialog_icon)
                    .setViewSelect(item.color)
            } else {
                holder.getView<MyColorCircleView>(R.id.item_todo_dialog_icon).setView(item.color)
            }
        }
    }
}