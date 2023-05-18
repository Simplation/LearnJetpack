package com.simplation.mvvm.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.simplation.mvvm.R
import com.simplation.mvvm.app.ext.setAdapterAnimation
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.data.model.bean.ArticleResponse

/**
 * Share adapter
 *
 * @constructor
 *
 * @param data
 */
class ShareAdapter(data: ArrayList<ArticleResponse>) :
    BaseQuickAdapter<ArticleResponse, BaseViewHolder>(
        R.layout.item_share_ariticle, data
    ) {

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: ArticleResponse) {
        // 赋值
        item.run {
            holder.setText(R.id.item_share_title, title)
            holder.setText(R.id.item_share_date, niceDate)
        }
    }
}
