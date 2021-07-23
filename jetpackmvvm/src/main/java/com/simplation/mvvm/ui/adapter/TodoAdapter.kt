package com.simplation.mvvm.ui.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.simplation.mvvm.R
import com.simplation.mvvm.app.ext.setAdapterAnimation
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.data.model.bean.TodoResponse
import com.simplation.mvvm.data.model.enums.TodoType


/**
 * Todo adapter
 *
 * @constructor
 *
 * @param data
 */
class TodoAdapter(data: ArrayList<TodoResponse>) :
    BaseQuickAdapter<TodoResponse, BaseViewHolder>(R.layout.item_todo, data) {

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun convert(holder: BaseViewHolder, item: TodoResponse) {
        // 赋值
        item.run {
            holder.setText(R.id.item_todo_title, title)
            holder.setText(R.id.item_todo_content, content)
            holder.setText(R.id.item_todo_date, dateStr)
            if (status == 1) {
                // 已完成
                holder.setVisible(R.id.item_todo_status, true)
                holder.setImageResource(R.id.item_todo_status, R.mipmap.ic_done)
                holder.getView<CardView>(R.id.item_todo_cardview).foreground =
                    context.getDrawable(R.drawable.forground_shap)
            } else {
                if (isDone()) {
                    // 未完成并且超过了预定完成时间
                    holder.setVisible(R.id.item_todo_status, true)
                    holder.setImageResource(R.id.item_todo_status, R.mipmap.ic_yiguoqi)
                    holder.getView<CardView>(R.id.item_todo_cardview).foreground =
                        context.getDrawable(R.drawable.forground_shap)
                } else {
                    // 未完成
                    holder.setVisible(R.id.item_todo_status, false)
                    TypedValue().apply {
                        context.theme.resolveAttribute(R.attr.selectableItemBackground, this, true)
                    }.run {
                        holder.getView<CardView>(R.id.item_todo_cardview).foreground =
                            context.getDrawable(resourceId)
                    }
                }
            }
            holder.getView<ImageView>(R.id.item_todo_tag).imageTintList =
                SettingUtil.getOneColorStateList(
                    TodoType.byType(priority).color
                )
        }
    }
}