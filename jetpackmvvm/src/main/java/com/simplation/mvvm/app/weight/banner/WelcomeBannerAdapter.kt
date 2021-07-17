package com.simplation.mvvm.app.weight.banner

import android.view.View
import com.simplation.mvvm.R
import com.zhpan.bannerview.BaseBannerAdapter

/**
 * Welcome banner adapter
 *
 * @constructor Create empty Welcome banner adapter
 */
class WelcomeBannerAdapter :BaseBannerAdapter<String, WelcomeBannerViewHolder>() {
    override fun createViewHolder(itemView: View, viewType: Int): WelcomeBannerViewHolder {
        return WelcomeBannerViewHolder(itemView)
    }

    override fun onBind(
        holder: WelcomeBannerViewHolder?,
        data: String?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_itemwelcome
    }

}