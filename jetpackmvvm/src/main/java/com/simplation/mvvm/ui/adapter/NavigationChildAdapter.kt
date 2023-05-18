package com.simplation.mvvm.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.simplation.mvvm.R
import com.simplation.mvvm.app.ext.setAdapterAnimation
import com.simplation.mvvm.app.util.ColorUtil
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.data.model.bean.ArticleResponse
import com.simplation.mvvmlib.ext.util.toHtml

/**
 * Navigation child adapter
 *
 * @constructor
 *
 * @param data
 */
class NavigationChildAdapter(data: ArrayList<ArticleResponse>) :
    BaseQuickAdapter<ArticleResponse, BaseViewHolder>(
        R.layout.flow_layout, data
    ) {

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: ArticleResponse) {
        holder.setText(R.id.flow_tag, item.title.toHtml())
        holder.setTextColor(R.id.flow_tag, ColorUtil.randomColor())
    }

}