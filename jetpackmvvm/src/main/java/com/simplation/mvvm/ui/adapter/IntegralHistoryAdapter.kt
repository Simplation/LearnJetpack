package com.simplation.mvvm.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.simplation.mvvm.R
import com.simplation.mvvm.app.ext.setAdapterAnimation
import com.simplation.mvvm.app.util.DatetimeUtil
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.data.model.bean.IntegralHistoryResponse

/**
 * Integral history adapter
 *
 * @constructor
 *
 * @param data
 */
class IntegralHistoryAdapter(data: ArrayList<IntegralHistoryResponse>) :
    BaseQuickAdapter<IntegralHistoryResponse, BaseViewHolder>(
        R.layout.item_integral_history,
        data
    ) {

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: IntegralHistoryResponse) {
        // 赋值
        item.run {
            val descStr =
                if (desc.contains("积分")) desc.subSequence(desc.indexOf("积分"), desc.length) else ""
            holder.setText(R.id.item_integralhistory_title, reason + descStr)
            holder.setText(
                R.id.item_integralhistory_date,
                DatetimeUtil.formatDate(date, DatetimeUtil.DATE_PATTERN_SS)
            )
            holder.setText(R.id.item_integralhistory_count, "+$coinCount")
            holder.setTextColor(R.id.item_integralhistory_count, SettingUtil.getColor(context))
        }
    }
}