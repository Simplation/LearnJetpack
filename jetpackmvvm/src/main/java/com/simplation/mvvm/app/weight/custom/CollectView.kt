package com.simplation.mvvm.app.weight.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.blankj.utilcode.util.VibrateUtils
import com.simplation.mvvm.R
import com.simplation.mvvm.app.util.CacheUtil
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import per.goweii.reveallayout.RevealLayout

/**
 * Collect view
 *
 * @constructor
 *
 * @param context
 * @param attrs
 * @param defaultAttr
 */
class CollectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultAttr: Int = 0
) : RevealLayout(context, attrs, defaultAttr), View.OnTouchListener {

    private var onCollectViewClickListener: OnCollectViewClickListener? = null

    override fun initAttr(attrs: AttributeSet?) {
        super.initAttr(attrs)
        setCheckWithExpand(true)
        setUncheckWithExpand(false)
        setCheckedLayoutId(R.layout.layout_collect_view_checked)
        setUncheckedLayoutId(R.layout.layout_collect_view_unchecked)
        setAnimDuration(300)
        setAllowRevert(true)
        setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                // 震动一下
                VibrateUtils.vibrate(40)
                if (CacheUtil.isLogin()) {
                    onCollectViewClickListener?.onClick(this)
                } else {
                    isChecked = true
                    nav(v).navigateAction(R.id.action_to_loginFragment)
                }
            }
        }
        return false
    }

    fun setOnCollectViewClickListener(onCollectViewClickListener: OnCollectViewClickListener) {
        this.onCollectViewClickListener = onCollectViewClickListener
    }

    interface OnCollectViewClickListener {
        fun onClick(v: CollectView)
    }

}