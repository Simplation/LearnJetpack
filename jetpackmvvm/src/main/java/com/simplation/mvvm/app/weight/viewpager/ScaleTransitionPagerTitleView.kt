package com.simplation.mvvm.app.weight.viewpager

import android.content.Context
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * Scale transition pager title view
 *
 * @constructor
 *
 * @param context
 */
class ScaleTransitionPagerTitleView(context: Context) : ColorTransitionPagerTitleView(context) {

    var minScale = 0.7f

    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
        super.onEnter(index, totalCount, enterPercent, leftToRight)    // 实现颜色渐变
        scaleX = minScale + (1.0f - minScale) * enterPercent
        scaleY = minScale + (1.0f - minScale) * enterPercent
    }

    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
        super.onLeave(index, totalCount, leavePercent, leftToRight)    // 实现颜色渐变
        scaleX = 1.0f + (minScale - 1.0f) * leavePercent
        scaleY = 1.0f + (minScale - 1.0f) * leavePercent
    }
}
