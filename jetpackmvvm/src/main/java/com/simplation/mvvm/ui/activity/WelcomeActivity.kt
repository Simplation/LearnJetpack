package com.simplation.mvvm.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.simplation.mvvm.R
import com.simplation.mvvm.app.base.BaseActivity
import com.simplation.mvvm.app.util.CacheUtil
import com.simplation.mvvm.app.util.SettingUtil
import com.simplation.mvvm.app.weight.banner.WelcomeBannerViewHolder
import com.simplation.mvvm.databinding.ActivityWelcomeBinding
import com.simplation.mvvm.app.weight.banner.WelcomeBannerAdapter
import com.simplation.mvvmlib.base.viewmodel.BaseViewModel
import com.simplation.mvvmlib.ext.view.gone
import com.simplation.mvvmlib.ext.view.visible
import com.zhpan.bannerview.BannerViewPager

/**
 * Welcome activity
 *
 * @constructor Create empty Welcome activity
 */
class WelcomeActivity : BaseActivity<BaseViewModel, ActivityWelcomeBinding>() {

    private var resList = arrayOf("A", "B", "C")

    private lateinit var mViewPager:BannerViewPager<String, WelcomeBannerViewHolder>

    override fun layoutId(): Int = R.layout.activity_welcome

    override fun initView(savedInstanceState: Bundle?) {
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT == 0) {
            finish()
            return
        }
        mDatabind.click = ProxyClick()
        mDatabind.welcomeBaseview.setBackgroundColor(SettingUtil.getColor(this))
        mViewPager = findViewById(R.id.banner_view)
        if (CacheUtil.isFirst()) {
            // 第一次进入应用
            mDatabind.welcomeImage.gone()
            mViewPager.apply {
                adapter = WelcomeBannerAdapter()
                setLifecycleRegistry(lifecycle)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        if (position == resList.size - 1) {
                            mDatabind.welcomeJoin.visible()
                        } else {
                            mDatabind.welcomeJoin.gone()
                        }
                    }
                })
                create(resList.toList())
            }
        } else {
            // 非第一次进入应用
            mDatabind.welcomeImage.visible()
            mViewPager.postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }, 300)
        }
    }

    inner class ProxyClick {
        fun toMain() {
            CacheUtil.setFirst(false)
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
            finish()
            // 添加动画
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}