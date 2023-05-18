package com.simplation.mvvm.ui.adapter

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.simplation.mvvm.R
import com.simplation.mvvm.app.ext.setAdapterAnimation
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.data.model.bean.SystemResponse
import com.simplation.mvvmlib.ext.util.toHtml

/**
 * System adapter
 *
 * @constructor
 *
 * @param data
 */
class SystemAdapter(data: ArrayList<SystemResponse>) : BaseQuickAdapter<SystemResponse, BaseViewHolder>(
    R.layout.item_system, data) {

    private var method: (data: SystemResponse, view: View, position: Int) -> Unit =
        { _: SystemResponse, _: View, _: Int -> }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: SystemResponse) {
        holder.setText(R.id.item_system_title, item.name.toHtml())
        holder.getView<RecyclerView>(R.id.item_system_rv).run {
            val foxayoutManager: FlexboxLayoutManager by lazy {
                FlexboxLayoutManager(context).apply {
                    // 方向 主轴为水平方向，起点在左端
                    flexDirection = FlexDirection.ROW
                    // 左对齐
                    justifyContent = JustifyContent.FLEX_START
                }
            }
            layoutManager = foxayoutManager
            setHasFixedSize(true)
            setItemViewCacheSize(200)
            isNestedScrollingEnabled = false
            adapter = SystemChildAdapter(item.children).apply {
                setOnItemClickListener { _, view, position ->
                    method(item, view, position)
                }
            }

        }
    }

    fun setChildClick(method: (data: SystemResponse, view: View, position: Int) -> Unit) {
        this.method = method
    }

}