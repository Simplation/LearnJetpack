package com.simplation.mvvm.app.weight.banner

import android.view.View
import android.widget.TextView
import com.simplation.mvvm.R
import com.zhpan.bannerview.BaseViewHolder

/**
 * Welcome banner view holder
 *
 * @constructor Create empty Welcome banner view holder
 */
class WelcomeBannerViewHolder(view: View) : BaseViewHolder<String>(view) {
    override fun bindData(data: String?, position: Int, pageSize: Int) {
        val textView = findView<TextView>(R.id.banner_text)
        textView.text = data
    }

}