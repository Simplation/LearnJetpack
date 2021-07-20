package com.simplation.mvvm.ui.fragment.me

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.ToastUtils
import com.simplation.mvvm.R
import com.simplation.mvvm.app.appViewModel
import com.simplation.mvvm.app.base.BaseFragment
import com.simplation.mvvm.app.ext.init
import com.simplation.mvvm.app.ext.joinQQGroup
import com.simplation.mvvm.app.ext.jumpByLogin
import com.simplation.mvvm.app.ext.setUiTheme
import com.simplation.mvvm.data.model.bean.BannerResponse
import com.simplation.mvvm.data.model.bean.IntegralResponse
import com.simplation.mvvm.databinding.FragmentMeBinding
import com.simplation.mvvm.viewmodel.request.RequestMeViewModel
import com.simplation.mvvm.viewmodel.state.MeViewModel
import com.simplation.mvvmlib.ext.nav
import com.simplation.mvvmlib.ext.navigateAction
import com.simplation.mvvmlib.ext.parseState
import com.simplation.mvvmlib.ext.util.notNull

/**
 * Me fragment
 *
 * @constructor Create empty Me fragment
 */
class MeFragment : BaseFragment<MeViewModel, FragmentMeBinding>() {

    private var rank: IntegralResponse? = null

    private val requestMeViewModel: RequestMeViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_me

    override fun initView(savedInstanceState: Bundle?) {
        mDataBind.vm = mViewModel
        mDataBind.click = ProxyClick()
        appViewModel.appColor.value?.let {
            setUiTheme(
                it,
                mDataBind.meLinear,
                mDataBind.meIntegral
            )
        }
        appViewModel.userInfo.value?.let { mViewModel.name.set(if (it.nickname.isEmpty()) it.username else it.nickname) }

        mDataBind.meSwipe.init {
            requestMeViewModel.getIntegral()
        }
    }

    override fun lazyLoadData() {
        appViewModel.userInfo.value?.run {
            mDataBind.meSwipe.isRefreshing = true
            requestMeViewModel.getIntegral()
        }
    }

    override fun createObserver() {
        requestMeViewModel.meData.observe(viewLifecycleOwner, { resultState ->
            mDataBind.meSwipe.isRefreshing = false
            parseState(resultState,
                {
                    rank = it
                    mViewModel.info.set("id：${it.userId}, 排名：${it.rank}")
                    mViewModel.integral.set(it.coinCount)
                },
                {
                    ToastUtils.showShort(it.errMsg)
                })
        })

        appViewModel.run {
            appColor.observe(this@MeFragment) {
                setUiTheme(it, mDataBind.meLinear, mDataBind.meSwipe, mDataBind.meIntegral)
            }

            userInfo.observe(this@MeFragment) { it ->
                it.notNull({
                    mDataBind.meSwipe.isRefreshing = true
                    mViewModel.name.set(if (it.nickname.isEmpty()) it.username else it.nickname)
                    requestMeViewModel.getIntegral()
                }, {
                    mViewModel.name.set("请先登录...")
                    mViewModel.info.set("id：--, 排名：--")
                    mViewModel.integral.set(0)
                })
            }
        }
    }

    inner class ProxyClick {
        // 登录
        fun login() {
            nav().jumpByLogin {}
        }

        // 积分
        fun integral() {
            nav().jumpByLogin {
                it.navigateAction(
                    R.id.action_mainfragment_to_integralFragment,
                    Bundle().apply { putParcelable("rank", rank) }
                )
            }
        }

        // 收藏
        fun collect() {
            nav().jumpByLogin { it.navigateAction(R.id.action_mainfragment_to_collectFragment) }
        }

        // 文章
        fun ariticle() {
            nav().jumpByLogin { it.navigateAction(R.id.action_mainfragment_to_ariticleFragment) }
        }

        fun todo() {
            nav().jumpByLogin { it.navigateAction(R.id.action_mainfragment_to_todoListFragment) }
        }

        // 玩 Android 开源网站
        fun about() {
            nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                putParcelable(
                    "bannerdata",
                    BannerResponse(
                        title = "玩 Android 网站",
                        url = "https://www.wanandroid.com/"
                    )
                )
            })
        }

        // 加入我们
        fun join() {
            joinQQGroup("9n4i5sHt4189d4DvbotKiCHy-5jZtD4D")
        }

        // 设置
        fun setting() {
            nav().navigateAction(R.id.action_mainfragment_to_settingFragment)
        }

        // demo
        fun demo() {
            nav().navigateAction(R.id.action_to_demoFragment)
        }
    }

}


