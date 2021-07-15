package com.simplation.mvvm.app.weight.banner

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.simplation.mvvm.R
import com.simplation.mvvm.data.model.bean.BannerResponse
import com.simplation.mvvmlib.base.appContext
import com.zhpan.bannerview.BaseViewHolder

/**
 * Home banner view holder
 *
 * @constructor
 *
 * @param view
 */
class HomeBannerViewHolder(view: View) : BaseViewHolder<BannerResponse>(view) {
    override fun bindData(data: BannerResponse?, position: Int, pageSize: Int) {
        val img = itemView.findViewById<ImageView>(R.id.bannerhome_img)
        data?.let {
            Glide.with(appContext)
                .load(it.url)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(img)
        }
    }
}