package com.simplation.mvvm.app.weight.banner

import android.view.View
import com.simplation.mvvm.R
import com.simplation.mvvm.data.model.bean.BannerResponse
import com.zhpan.bannerview.BaseBannerAdapter

/**
 * Home banner adapter
 *
 * @constructor Create empty Home banner adapter
 */
class HomeBannerAdapter : BaseBannerAdapter<BannerResponse, HomeBannerViewHolder>() {

    override fun createViewHolder(itemView: View, viewType: Int): HomeBannerViewHolder {
        return HomeBannerViewHolder(itemView)
    }

    override fun onBind(
        holder: HomeBannerViewHolder?,
        data: BannerResponse?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_itemhome
    }
}