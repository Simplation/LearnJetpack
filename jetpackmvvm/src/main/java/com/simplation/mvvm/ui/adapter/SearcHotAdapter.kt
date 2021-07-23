package com.simplation.mvvm.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.simplation.mvvm.R
import com.simplation.mvvm.app.ext.setAdapterAnimation
import com.simplation.mvvm.app.util.ColorUtil
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.data.model.bean.SearchResponse

/**
 * Searc hot adapter
 *
 * @constructor
 *
 * @param data
 */
class SearcHotAdapter(data: ArrayList<SearchResponse>) :
    BaseQuickAdapter<SearchResponse, BaseViewHolder>(
        R.layout.flow_layout, data
    ) {

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: SearchResponse) {
        holder.setText(R.id.flow_tag, item.name)
        holder.setTextColor(R.id.flow_tag, ColorUtil.randomColor())
    }
}